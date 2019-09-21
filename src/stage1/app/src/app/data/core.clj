(ns app.data.core
  (:require [clojure.repl :refer :all]
            [clojure.java.shell :refer [sh] :as shell]))

(comment

  (shell/sh "ls")

  (shell/sh "pwd")

  (shell/sh "echo 3")
  shell/*sh-dir*

  (shell/with-sh-dir)

  (shell/sh "lein compile")

  (sh "echo $(pwd)")
  (sh "bash -c \"ls\"")
  (sh "ls /usr/bin/java")
  

  (when-not (.exists (io/file (str data-dir)))
    (do (println "Retrieving data ...") (sh "./get_data.sh")))

  (when-not (.exists (io/file (str data-dir "train-images-idx3-ubyte")))
    (sh "bash get_mnist_data.sh"))

  ;
  )


