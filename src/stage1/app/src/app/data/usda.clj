(ns app.data.usda
  (:require [clojure.repl :refer :all]
            [clojure.java.shell :refer [sh] :as shell]
            [clojure.java.io :as io]
            [clojure.data.csv :as csv]
            [clojure.string]))

; (prn "---using .java.shell to list directory:")
; (sh "ls /")

(def data-dir "/opt/data/")
(def data-dir-out "/opt/data/stage1/")


(def DATA_SRC (str data-dir "sr28asc/DATA_SRC.txt"))
(def FOOD_DES (str data-dir "sr28asc/FOOD_DES.txt"))
(def NUTR_DEF (str data-dir "sr28asc/NUTR_DEF.txt"))
(def NUT_DATA (str data-dir "sr28asc/NUT_DATA.txt"))

(def FOOD_DES-out (str data-dir-out "food-des.edn"))
(def NUTR_DEF-out (str data-dir-out "nutr-def.edn"))
(def NUT_DATA-out (str data-dir-out "nut-data.edn"))


(when-not (.exists (io/file (str data-dir "sr28asc")))
  (sh "bash "))

(comment

  (.exists (io/file DATA_SRC))

  (read-csv-file DATA_SRC)

  (clojure.string/split "3^3^^" #"\^" 100)

  (source clojure.string/split)

  ;
  )

(defn read-src-file
  [filename-in filename-out & {:keys [limit]}]
  (with-open [reader (io/reader filename-in)
              writer (io/writer filename-out :append true)
              ]
    (let [data (line-seq reader)
          lines data]
      (doseq [line lines]
        (let [line-out (str line \newline)]
          (.write writer line-out))
        ;
        ))))

(comment
  
  (read-src-file NUTR_DEF NUTR_DEF-out )
  
  ;
  )





