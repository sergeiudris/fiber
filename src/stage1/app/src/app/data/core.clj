(ns app.data.core
  (:require [clojure.repl :refer :all]
            [clojure.java.shell :refer [sh] :as shell]
            [clojure.java.io :as io]
            [clojure.data.csv :as csv]
            [clojure.string]))

; (prn "---using .java.shell to list directory:")
; (sh "ls /")

(def data-dir "/opt/data/")

(comment

  (sh "ls")

  (sh "pwd" :dir "/opt/data")

  (sh "ls" :dir "/opt")
  (sh "ls" :dir "/opt/data")
  (sh "ls" :dir "/opt/app")

  (sh "ls" :dir "/opt/data")

  (sh "pwd")
  
  (sh "bash")

  shell/*sh-dir*

  (shell/with-sh-dir)

  (shell/sh "lein compile")

  (println (:out (sh "cowsay" "Printing a command-line output")))

  (sh "echo $(pwd)")
  (sh "bash -c \"ls\"")
  (sh "ls /usr/bin/java")


  (when-not (.exists (io/file (str data-dir)))
    (do (println "Retrieving data ...") (sh "./get_data.sh")))

  (when-not (.exists (io/file (str data-dir "train-images-idx3-ubyte")))
    (sh "bash get_mnist_data.sh"))

  ;
  )


(defn read-column [reader column-index]
  (let [data (csv/read-csv reader)]
    (map #(nth % column-index) data)))

(defn read-csv-file [filename]
  (with-open [reader (io/reader filename)]
    #_(prn reader)
    (->> (read-column reader 1)
         (drop 1)
         #_(map #(Double/parseDouble %))
         #_(reduce + 0))))

(defn csv-data->maps [csv-data]
  (map zipmap
       (->> (first csv-data) ;; First row is the header
            (map keyword) ;; Drop if you want string keys instead
            repeat)
       (rest csv-data)))






