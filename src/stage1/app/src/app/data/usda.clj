(ns app.data.usda
  (:require [clojure.repl :refer :all]
            [clojure.java.shell :refer [sh] :as shell]
            [clojure.java.io :as io]
            [clojure.data.csv :as csv]
            [clojure.string]
            [tools.io.core :refer [read-nth-line delete-files count-lines]]
            [tools.core :refer [try-parse-int try-parse-float]]))

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

(defn ascii-file->edn-file
  [filename-in filename-out line->edn & {:keys [limit]}]
  (with-open [reader (io/reader filename-in)
              writer (io/writer filename-out :append true)
              ]
    (let [data (line-seq reader)
          lines data]
      (.write writer (str "[" \newline))
      (doseq [line lines]
        (let [line-out (str (line->edn line) \newline)]
          (.write writer line-out)))
      (.write writer "]")
        ;
      )))

(defn ascii-line->vals
  [line]
  (let [elms (clojure.string/split line #"\^" 20)]
    (map (fn [elm]
           (as-> elm x
             (if (clojure.string/starts-with? x "~") (subs x 1)  x)
             (if (clojure.string/ends-with? x "~") (subs x 0 (dec (count x)))  x)))
         elms)))


(defn nutr-def->edn
  "Processes ascii file into edn file"
  []
  (ascii-file->edn-file
   NUTR_DEF
   NUTR_DEF-out
   (fn [line]
     (let [vals (ascii-line->vals line)]
       (into {}
             (filter (comp some? val)
                     {:usda.nutr/id (nth vals 0)
                      :usda.nutr/units (nth vals 1)
                      :usda.nutr/tagname (nth vals 2)
                      :usda.nutr/desc (nth vals 3)
                      :usda.nutr/num-dec (try-parse-int (nth vals 4))
                      :usda.nutr/sr-order (try-parse-int (nth vals 5))}))
       ;
       ))))



(comment
  
  (read-src-file NUTR_DEF NUTR_DEF-out )
  
  
  
  (nutr-def->edn)
  
  (delete-files NUTR_DEF-out)
  
  (def line (read-nth-line NUTR_DEF 1))
  (count-lines NUTR_DEF)
  
  (clojure.string/split line #"\^" 20)
  
  (ascii-line->vals line)
  
  
  
  (dir clojure.string)
  (source clojure.string/starts-with?)
  (source clojure.string/triml)

  ;
  )





