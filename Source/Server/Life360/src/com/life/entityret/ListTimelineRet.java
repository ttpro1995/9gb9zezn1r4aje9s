package com.life.entityret;

import com.life.common.JsonUtils;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class ListTimelineRet {

    public int total;
    public List<TimelineRet> timelines = new ArrayList<>();

    public ListTimelineRet() {
    }

    public void add(TimelineRet timelineRet) {
        timelines.add(timelineRet);
    }

    @Override
    public String toString() {
        return JsonUtils.Instance.toJson(this);
    }

}
