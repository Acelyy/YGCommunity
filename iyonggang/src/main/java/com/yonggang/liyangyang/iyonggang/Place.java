package com.yonggang.liyangyang.iyonggang;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liyangyang on 2017/7/7.
 */

public class Place {

    private Map<String, String> places = new HashMap<>();

    public Map<String, String> getPlaces() {
        return places;
    }

    public void setPlaces(Map<String, String> places) {
        this.places = places;

    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public PlaceMap getMap() {
        List<String> keys = new ArrayList<>();
        List<String> values = new ArrayList<>();
        for (Map.Entry<String, String> entry : places.entrySet()) {
            keys.add(entry.getKey());
            values.add(entry.getValue());
        }
        return new PlaceMap(keys, values);
    }

    public class PlaceMap {
        private List<String> keys = new ArrayList<>();
        private List<String> values = new ArrayList<>();

        public List<String> getKeys() {
            return keys;
        }

        public void setKeys(List<String> keys) {
            this.keys = keys;
        }

        public List<String> getValues() {
            return values;
        }

        public void setValues(List<String> values) {
            this.values = values;
        }

        public PlaceMap(List<String> keys, List<String> values) {
            this.keys = keys;
            this.values = values;
        }
    }
}
