(ns ui.dbquery.spec
  (:require [clojure.repl :as repl]
            [clojure.spec.alpha :as s]
            [clojure.test.check.generators]
            [clojure.spec.gen.alpha :as gen]
            [fiber.spec]))

(s/def ::uuid uuid?)
(s/def ::type keyword?)

(s/def ::entity (s/keys :req [::uuid
                              ::type] ))

(s/def :abc/uuid uuid?)
(s/def :abc/type keyword?)

(s/def :abc/entity (s/keys :req [:abc/uuid
                                     :abc/type]))


(defn gen-resource
  []
  {:fiber.spec/uuid (random-uuid)
   :fiber.spec/type :fiber/resource
   :fiber.spec/uri "https://en.wikipedia.org/wiki/Main_Page"
   :fiber.spec/upvotes 0
   :fiber.spec/downvotes 0
   :fiber.spec/score 50
   :fiber.spec/links
   []
   :fiber.spec/doc "a resource"})

(comment
  (gen/generate (s/gen int?))
  (gen/generate (s/gen ::entity))
  
  (repl/dir fiber.spec)
  (fiber.spec/ping)
  
  ;
  )



#_(s/valid? :abc/entity {:abc/uuid #uuid "a3312e9d-1bf4-47bb-a856-c295bad209f0"
                             :abc/type :resource})

#_(s/valid? :fiber.spec/resource {:fiber.spec/uuid #uuid "a3312e9d-1bf4-47bb-a856-c295bad209f0"
                                    :fiber.spec/type :resource})