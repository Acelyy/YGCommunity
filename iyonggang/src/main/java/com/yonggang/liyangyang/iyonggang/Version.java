package com.yonggang.liyangyang.iyonggang;

/**
 * Created by liyangyang on 2017/7/5.
 */

public class Version {

    /**
     * version : 1.0
     * force : 0
     * file : 192.168.0.224/android/
     * server : 192.168.0.224
     * feature : null
     * created : null
     * type : 2
     */

    private String version;
    private String force;
    private String file;
    private String server;
    private Object feature;
    private Object created;
    private String type;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getForce() {
        return force;
    }

    public void setForce(String force) {
        this.force = force;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public Object getFeature() {
        return feature;
    }

    public void setFeature(Object feature) {
        this.feature = feature;
    }

    public Object getCreated() {
        return created;
    }

    public void setCreated(Object created) {
        this.created = created;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
