package com.yonggang.ygcommunity.monitor.model

class MonitorModel {

    class GridCount {

        /**
         * phone : {"bjl":"127","blz":0,"sbm":"127","wbj":0,"ysl":0}
         * bbs : {"bjl":"1","blz":"0","sbm":"122","wbj":"121","ysl":"0"}
         * gird : {"bjl":0,"blz":0,"sbm":0,"wbj":0,"ysl":0}
         * total : {"bjl":128,"blz":"0","sbm":249,"wbj":"121","ysl":"0"}
         */

        var phone: Phone? = null
        var bbs: Bbs? = null
        var gird: Gird? = null
        var total: Total? = null

        class Phone {
            /**
             * bjl : 127
             * blz : 0
             * sbm : 127
             * wbj : 0
             * ysl : 0
             */

            var bjl: Int = 0
            var blz: Int = 0
            var sbm: Int = 0
            var wbj: Int = 0
            var ysl: Int = 0
        }

        class Bbs {
            /**
             * bjl : 1
             * blz : 0
             * sbm : 122
             * wbj : 121
             * ysl : 0
             */

            var bjl: Int = 0
            var blz: Int = 0
            var sbm: Int = 0
            var wbj: Int = 0
            var ysl: Int = 0
        }

        class Gird {
            /**
             * bjl : 0
             * blz : 0
             * sbm : 0
             * wbj : 0
             * ysl : 0
             */

            var bjl: Int = 0
            var blz: Int = 0
            var sbm: Int = 0
            var wbj: Int = 0
            var ysl: Int = 0
        }

        class Total {
            /**
             * bjl : 128
             * blz : 0
             * sbm : 249
             * wbj : 121
             * ysl : 0
             */

            var bjl: Int = 0
            var blz: Int = 0
            var sbm: Int = 0
            var wbj: Int = 0
            var ysl: Int = 0
        }
    }
}
