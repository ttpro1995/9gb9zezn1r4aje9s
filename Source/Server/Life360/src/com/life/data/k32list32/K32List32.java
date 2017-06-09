package com.life.data.k32list32;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.life.common.BytesUtils;
import com.life.common.Config;
import com.life.common.ECode;
import com.life.common.ECodeHelper;
import com.life.data.base.BaseRock;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class K32List32 extends BaseRock {

    private boolean usingSet;

    public K32List32(String name) {

        usingSet = Config.instance.getBoolean(K32List32.class, name, "usingSet", false);

        String namedb = Config.instance.getString(K32List32.class, name, "name", "");
        File dir = new File(Config.instance.getString(K32List32.class, name, "dir", "./"));
        boolean readonly = Config.instance.getBoolean(K32List32.class, name, "readonly", false);
        boolean allowConcurrentMemtableWrite = Config.instance.getBoolean(K32List32.class, name, "allowConcurrentMemtableWrite", true);

        super.init(namedb, dir, readonly, allowConcurrentMemtableWrite);

    }

    public boolean isUsingSet() {
        return usingSet;
    }

    /**
     *
     * @param key
     * @return not null, -NOT_EXIST | SUCCESS
     */
    public Value get(int key) {
        Value value = new Value(ECodeHelper.mkfail(ECode.NOT_EXIST));
        byte[] get = super.getValue(BytesUtils.getBytes(key));
        if (Objects.nonNull(get)) {
            value.setError(ECode.SUCCESS.getValue());

            ByteBuffer bb = ByteBuffer.wrap(get);
            int size = bb.array().length;
            int count = size / Integer.BYTES;
            value.setTotal(count);

            List<Integer> lst = new ArrayList<>(count);
            for (int i = 0; i < count; i++) {
                lst.add(bb.getInt());
            }
            value.setValue(lst);

        }
        return value;
    }

    /**
     * Get slice
     *
     * @param key
     * @param offset 0 -> ...
     * @param size
     * @return not exists | success
     */
    public Value get(int key, int offset, int size) {
        Value valueResult = new Value(ECodeHelper.mkfail(ECode.NOT_EXIST));

        byte[] get = super.getValue(BytesUtils.getBytes(key));
        if (Objects.nonNull(get)) {
            valueResult.setError(ECode.SUCCESS.getValue());

            ByteBuffer bb = ByteBuffer.wrap(get);
            int lengthData = bb.array().length;
            int totalValue = lengthData / Integer.BYTES;

            valueResult.setTotal(totalValue);

            if (offset > totalValue || size <= 0) {
                valueResult.setValue(new ArrayList<>());
                return valueResult;
            }
            if (offset < 0) {
                offset = 0;
            }

            if (offset < totalValue) {
                bb.position(offset * Integer.BYTES);
            } else {
                bb.position(lengthData);
            }

            if (offset + size > totalValue) {
                size = totalValue - offset;
            }

            List<Integer> lst = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                lst.add(bb.getInt());
            }
            valueResult.setValue(lst);

        }
        return valueResult;
    }

    /**
     *
     * @param key
     * @param offset
     * @param size
     * @return not null, SUCCESS , -NOT_EXIST
     */
    public Value getLast(int key, int offset, int size) {
        Value valueResult = new Value(ECodeHelper.mkfail(ECode.NOT_EXIST));

        byte[] get = super.getValue(BytesUtils.getBytes(key));
        if (Objects.nonNull(get)) {
            valueResult.setError(ECode.SUCCESS.getValue());

            ByteBuffer bb = ByteBuffer.wrap(get);
            int lengthDataByte = bb.array().length;

            int totalValue = lengthDataByte / Integer.BYTES;

            valueResult.setTotal(totalValue);
            if (size <= 0 || offset > totalValue) {
                valueResult.setValue(new ArrayList<>());
                return valueResult;
            }
            if (offset < 0) {
                offset = 0;
            }

            int sum = offset + size;
            if (sum > totalValue) {

                size = totalValue - offset;
                offset = 0;
            } else {
                offset = totalValue - sum;
            }
            bb.position(offset * Integer.BYTES);

            Integer[] arr = new Integer[size];

            for (int i = size - 1; i >= 0; i--) {
                arr[i] = bb.getInt();
            }

            valueResult.setValue(new ArrayList<>(Arrays.asList(arr)));

        }
        return valueResult;
    }

    /**
     * put or update
     *
     * @param key
     * @param value
     * @return true if success
     */
    public boolean put(int key, List<Integer> value) {
        ByteBuffer allocate = ByteBuffer.allocate(value.size() * Integer.BYTES);
        value.forEach((v) -> {
            allocate.putInt(v);
        });
        return super.putValue(BytesUtils.getBytes(key), allocate.array());

    }

    /**
     * if exists data, add last. if not exists data, put new
     *
     * @param key
     * @param value
     * @return position(1 - &gt ...) | -already_exists with Set | -fail
     */
    public int putEntries(int key, int value) {
        int error;
        byte[] serializeKey = BytesUtils.getBytes(key);
        boolean put;
        if (usingSet) {
            Value get = this.get(key);
            List<Integer> list;
            if (ECodeHelper.isSuccess(get.error)) {
                list = get.value;
                if (!list.contains(value)) {
                    list.add(value);
                    error = list.size();
                } else {
                    return ECodeHelper.mkfail(ECode.ALREADY_EXIST);
                }
            } else {
                list = new ArrayList<>();
                list.add(value);
                error = 1;
            }
            put = this.put(key, list);
        } else {
            byte[] get = super.getValue(serializeKey);
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            if (get != null) {
                out.write(get);
            }
            out.writeInt(value);
            byte[] data = out.toByteArray();
            put = super.putValue(serializeKey, data);
            error = (data.length / Integer.BYTES);
        }
        if (put) {
            return error;
        }
        return -ECode.FAIL.getValue();
    }

    /**
     *
     * @param key
     * @param value
     * @return number value remove | not exists | fail
     */
    public int remove(int key, int value) {
        Value get = this.get(key);
        if (ECodeHelper.isFail(get.error)) {
            return get.error;
        }
        List list = get.value;

        int error = 0;
        if (usingSet) {
            if (list.remove((Object) value)) {
                error++;
            }
        } else {
            while (list.remove((Object) value)) {
                error++;
            }
        }
        if (error > 0) {
            if (this.put(key, list)) {
                return error;
            } else {
                error = -ECode.FAIL.getValue();
            }
        } else {
            error = -ECode.NOT_EXIST.getValue();
        }

        return error;
    }

    /**
     * delete from db
     *
     * @param key
     * @return
     */
    public boolean delete(int key) {
        return super.deleteValue(BytesUtils.getBytes(key));
    }

    /**
     *
     * @param key
     * @param value
     * @return SUCCESS, -NOT_CONTAIN, -NOT_EXIST
     */
    public int contain(int key, int value) {
        Value get = this.get(key);
        if (ECodeHelper.isFail(get.error)) {
            return get.error;
        }
        List list = get.value;

        if (list.contains(value)) {
            return ECode.SUCCESS.getValue();
        } else {
            return -ECode.NOT_CONTAIN.getValue();
        }

    }

}
