package com.life.entityret;

import com.life.common.JsonUtils;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class ListAppointmentRet {

    public long total;
    public List<AppointmentRet> appointments = new ArrayList<>();

    public ListAppointmentRet() {

    }

    public void add(AppointmentRet appointmentRet) {
        appointments.add(appointmentRet);
    }

    @Override
    public String toString() {
        return JsonUtils.Instance.toJson(this);
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

}
