(ns fiber.spec
  (:require [clojure.repl :as repl]
            [clojure.spec.alpha :as s]
            [clojure.test.check.generators]
            [clojure.spec.gen.alpha :as gen]))

(defn ping
  []
  'pong)

(s/def ::uuid uuid?)
(s/def ::type keyword?)
(s/def ::uri string?)
(s/def ::upvotes int?)
(s/def ::downvotes int?)
(s/def ::score number?)
(s/def ::links vector?)
(s/def ::doc string?)


(s/def ::resource (s/keys :req [::uuid
                                ::type
                                ::uri
                                ::upvotes
                                ::downvotes
                                ::score
                                ::links
                                ::doc]))



#_(s/valid? :fiber.spec/resource {:fiber.spec/uuid #uuid "a3312e9d-1bf4-47bb-a856-c295bad209f0"
                                    :fiber.spec/type "resource"})

(s/def ::ent (s/keys  :req [::uuid
                            ::type]))

(defn gen-ent
  []
  (gen/generate (s/gen ::resource)))

(comment
  (repl/dir s)
  (s/def nsp1-att1 int?)

  (s/def obj1 (s/keys :req [nsp1-att1]))
  
  ;
  )

