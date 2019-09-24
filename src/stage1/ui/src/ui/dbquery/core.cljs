(ns ui.dbquery.core
  (:require [cljs.repl :as repl])
  )

; global (in ui.cred module) editor instance reference
(def editor-query (atom nil))

(def editor-query-results (atom nil))

