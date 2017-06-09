package com.life.data.base;

import java.io.Serializable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author truongnguyenax@gmail.com
 * @param <K>
 */
public abstract class AbstractKeyList32<K extends Serializable> extends BaseRock {

    static final Logger LOGGER = LogManager.getLogger(AbstractKeyList32.class);

    private boolean usingSet;

    public AbstractKeyList32() {
    }

//    public boolean isUsingSet() {
//        return usingSet;
//    }
//
//    public void setUsingSet(boolean usingSet) {
//        this.usingSet = usingSet;
//    }
//
//    /**
//     * 
//     * @param key
//     * @return not exists | success
//     */
//    public ValueResult get(K key) {
//        ValueResult valueResult = new ValueResult(ECodeHelper.mkfail(ECode.NOT_EXIST));
//        byte[] get = super.getValue(SerializationUtils.serialize(key));
//        if (Objects.nonNull(get)) {
//            valueResult.setError(ECode.SUCCESS.getValue());
//            Value value = new Value();
//            valueResult.setValue(value);
//
//            ByteBuffer bb = ByteBuffer.wrap(get);
//            int size = bb.limit();
//            int count = size / 4;
//            value.setTotal(count);
//
//            List<Integer> lst = new ArrayList<>(count);
//            for (int i = 0; i < count; i++) {
//                lst.add(bb.getInt());
//            }
//            value.setValue(lst);
//
//        }
//        return valueResult;
//    }
//
//    /**
//     * Get slice
//     *
//     * @param key
//     * @param offset 0 -> ...
//     * @param size
//     * @return not exists | success 
//     */
//    public ValueResult get(K key, int offset, int size) {
//        ValueResult valueResult = new ValueResult(ECodeHelper.mkfail(ECode.NOT_EXIST));
//
//        byte[] get = super.getValue(SerializationUtils.serialize(key));
//        if (Objects.nonNull(get)) {
//            valueResult.setError(ECode.SUCCESS.getValue());
//
//            ByteBuffer bb = ByteBuffer.wrap(get);
//            int length = bb.array().length;
//
//            bb.position(offset * Integer.BYTES);
//            int total = length / Integer.BYTES;
//
//            Value value = new Value();
//            valueResult.setValue(value);
//            value.setTotal(total);
//
//            int c = total - offset;
//            if (size > c) {
//                size = c;
//            }
//            List<Integer> lst = new ArrayList<>(size);
//            for (int i = 0; i < size; i++) {
//                lst.add(bb.getInt());
//            }
//            value.setValue(lst);
//
//        }
//        return valueResult;
//    }
//
//    /**
//     * put or update
//     *
//     * @param key
//     * @param value
//     * @return
//     */
//    protected boolean put(K key, List<Integer> value) {
//        ByteBuffer allocate = ByteBuffer.allocate(value.size() * Integer.BYTES);
//        for (int v : value) {
//            allocate.putInt(v);
//        }
//        return super.putValue(SerializationUtils.serialize(key), allocate.array());
//
//    }
//
//    /**
//     * if exists data, add last. if not exists data, put new
//     *
//     * @param key
//     * @param value
//     * @return
//     */
//    public int putEntries(K key, int value) {
//        int error = -ECode.FAIL.getValue();
//        byte[] serializeKey = SerializationUtils.serialize(key);
//        boolean put;
//        if (usingSet) {
//            ValueResult get = this.get(key);
//            List<Integer> list;
//            if (ECodeHelper.isSuccess(get.error)) {
//                list = get.value.value;
//                if (!list.contains(value)) {
//                    list.add(value);
//                } else {
//                    return ECodeHelper.mkfail(ECode.ALREADY_EXIST);
//                }
//            } else {
//                list = new ArrayList<>();
//                list.add(value);
//            }
//            put = this.put(key, list);
//        } else {
//            byte[] get = super.getValue(serializeKey);
//            ByteArrayDataOutput out = ByteStreams.newDataOutput();
//            if (get != null) {
//                out.write(get);
//            }
//            out.writeInt(value);
//            put = super.putValue(serializeKey, out.toByteArray());
//        }
//        if (put) {
//            error = ECode.SUCCESS.getValue();
//        }
//
//        return error;
//    }
//
//    /**
//     * 
//     * @param key
//     * @param value
//     * @return number value remove
//     */
//    public int remove(K key, int value) {
//        ValueResult get = this.get(key);
//        if (ECodeHelper.isFail(get.error)) {
//            return get.error;
//        }
//        List list = get.value.getValue();
//
//        int error = 0;
//        if (usingSet) {
//            if (list.remove((Object) value)) {
//                error++;
//            }
//        } else {
//            while (list.remove((Object) value)) {
//                error++;
//            }
//        }
//        return error;
//    }
//
//    public boolean delete(K key) {
//        return super.deleteValue(SerializationUtils.serialize(key));
//    }

}
