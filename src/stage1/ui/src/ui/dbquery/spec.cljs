(ns ui.dbquery.spec
  (:require [clojure.repl :as repl]
            [clojure.spec.alpha :as s]
            [clojure.test.check.generators]
            [clojure.spec.gen.alpha :as gen]
            ))

(s/def ::uuid uuid?)
(s/def ::type keyword?)

(s/def ::entity (s/keys :req [::uuid
                              ::type] ))

(s/def :abc/uuid uuid?)
(s/def :abc/type keyword?)

(s/def :abc/entity (s/keys :req [:abc/uuid
                                     :abc/type]))




#_(s/valid? :abc/entity {:abc/uuid #uuid "a3312e9d-1bf4-47bb-a856-c295bad209f0"
                             :abc/type :resource})
