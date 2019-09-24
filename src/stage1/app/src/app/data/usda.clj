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
          lines (if limit (take limit data) data)]
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

(defn food-des->edn
  "Processes ascii file into edn file"
  []
  (ascii-file->edn-file
   FOOD_DES
   FOOD_DES-out
   (fn [line]
     (let [vals (ascii-line->vals line)]
       (into {}
             (filter (comp some? val)
                     {:usda.item/id (nth vals 0)
                      :usda.item/group-id (nth vals 1)
                      :usda.item/desc-long (nth vals 2)
                      :usda.item/desc-short (nth vals 3)
                      :usda.item/com-name (nth vals 4)
                      :usda.item/manufac-name (nth vals 5)
                      :usda.item/survey (if (= (nth vals 6) "Y") true false)
                      :usda.item/ref-desc (nth vals 7)
                      :usda.item/refuse (try-parse-int (nth vals 8))
                      :usda.item/sci-name (nth vals 9)
                      :usda.item/n-factor (try-parse-float (nth vals 10))
                      :usda.item/pro-factor (try-parse-float (nth vals 11))
                      :usda.item/fat-factor (try-parse-float (nth vals 12))
                      :usda.item/cho-factor (try-parse-float (nth vals 13))
                      }))
       ;
       ))))

(defn nut-data->edn
  "Processes ascii file into edn file"
  []
  (ascii-file->edn-file
   NUT_DATA
   NUT_DATA-out
   (fn [line]
     (let [vals (ascii-line->vals line)]
       (into {}
             (filter (comp some? val)
                     {:usda.item/_nutrients [:usda.item/id (nth vals 0)]
                      :usda.nutrdata/id (str (nth vals 0) "-" (nth vals 1))
                      :usda.nutrdata/item-id (nth vals 0)
                      :usda.nutrdata/nutr-id (nth vals 1)
                      :usda.nutrdata/nutr-val (try-parse-float (nth vals 2))
                      :usda.nutrdata/num-data-points (try-parse-float (nth vals 3))
                      :usda.nutrdata/std-error (try-parse-float (nth vals 4))
                      :usda.nutrdata/src-cd (nth vals 5)
                      :usda.nutrdata/deriv-cd (nth vals 6)
                      :usda.nutrdata/ref-ndb-no (nth vals 7)
                      :usda.nutrdata/add-nutr-mark (nth vals 8)
                      :usda.nutrdata/num-studies (try-parse-int (nth vals 9))
                      :usda.nutrdata/min (try-parse-float (nth vals 10))
                      :usda.nutrdata/max (try-parse-float (nth vals 11))
                      :usda.nutrdata/df (try-parse-int (nth vals 12))
                      :usda.nutrdata/low-eb (try-parse-float (nth vals 13))
                      :usda.nutrdata/up-eb (try-parse-float (nth vals 14))
                      :usda.nutrdata/stat-cmt (nth vals 15)
                      :usda.nutrdata/addmod-date (nth vals 16)
                      :usda.nutrdata/cc (nth vals 17)}))
       ;
       ))
  ;  :limit 10
   ))

(defn create-files!
  []
  (try
    (do
      (time (nutr-def->edn))
      (time (food-des->edn))
      (time (nut-data->edn)))
    (catch Exception e (do (prn (.getMessage e)) false))))

(defn del-files!
  []
  (try
    (do
      (delete-files NUTR_DEF-out FOOD_DES-out NUT_DATA-out))
    (catch Exception e (do (prn (.getMessage e)) false))))

(comment

  (read-src-file NUTR_DEF NUTR_DEF-out)

  (try-parse-float "")

  (time (nutr-def->edn))
  #_(delete-files NUTR_DEF-out)

  (time (food-des->edn))
  #_(delete-files FOOD_DES-out)
  
  (time (nut-data->edn)) ; 298 mb
  #_(delete-files NUT_DATA-out)
  (count-lines NUT_DATA) ; 40.4 mb
  
  #_(create-files!)
  

  (def line (read-nth-line NUTR_DEF 1))
  (count-lines NUTR_DEF)

  (def line (read-nth-line FOOD_DES 35))
  
  (def linev (clojure.string/split line #"\^" 20))

  (count linev)

  (ascii-line->vals line)



  (dir clojure.string)
  (source clojure.string/starts-with?)
  (source clojure.string/triml)

  ;
  )





