(ns app.main
  (:require [tools.nrepl]
            [tools.core]
            [tools.io.core]
            [app.srv.server]
            [tools.core]
            [fiber.core]
   ;
            )
  ;
  )


(defn -main  [& args]
  (tools.nrepl/-main)
  (app.srv.server/run-dev)
  )

#_(defn -main  [& args]
  (tools.nrepl/-main)
  (app.srv.server/-main)
  )

(comment

  (tools.core/try-parse-int "3")

  (tools.core/version)

  
  (fiber.core/priority)

  ;
  )

