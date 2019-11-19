package com.highgreat.education.bean;

import java.util.List;

/**
 * Do Good App
 * 项目名称：简单的可多选相册
 * 类描述：
 * 创建人：mac-likh
 * 创建时间：16/1/19 09:02
 * 修改人：mac-likh
 * 修改时间：16/1/19 09:02
 * 修改备注：
 */
public class ImagesBean {

    /**
     * data : [{    "path":"http://192.168.1.1:80/test/emmc_sdcard/pic/Image-30.jpg",
     *              "size":"165.2KB",
     *              "thumb":"http://192.168.1.1:80/test/emmc_sdcard/pic/Image-30_thumb.jpg",
     *               "created":"1970-01-01 03:43:43",
     *              "title":"Image-30.jpg"}
     * totalPage : 1
     * currentPage : 1
     */
    private List<DataEntity> data;
    private int totalPage;
    private int currentPage;

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public List<DataEntity> getData() {
        return data;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public static class DataEntity {
        /**
         * path : http://192.168.1.1:80/test/emmc_sdcard/pic/Image-30.jpg
         * size : 165.2KB
         * thumb : http://192.168.1.1:80/test/emmc_sdcard/pic/Image-30_thumb.jpg
         * created : 1970-01-01 03:43:43
         * title : Image-30.jpg
         */
        private String path;
        private String size;
        private String thumb;
        private String created;
        private String title;

        public void setPath(String path) {
            this.path = path;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPath() {
            return path;
        }

        public String getSize() {
            return size;
        }

        public String getThumb() {
            return thumb;
        }

        public String getCreated() {
            return created;
        }

        public String getTitle() {
            return title;
        }
    }
}
