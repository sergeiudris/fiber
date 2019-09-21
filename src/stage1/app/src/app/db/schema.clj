;   Copyright (c) Cognitect, Inc. All rights reserved.
;   The use and distribution terms for this software are covered by the
;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;   which can be found in the file epl-v10.html at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.

(ns app.db.schema
  (:require [datomic.api :as d]
            [app.db.core :refer [db conn]]
            [clojure.repl :refer :all]))



(comment 

;; find the idents of all schema elements in the system
(sort (d/q '[:find [?ident ...]
             :where [_ :db/ident ?ident]]
           db))


;; find just the attributes
(sort (d/q '[:find [?ident ...]
             :where 
             [?e :db/ident ?ident]
             [_ :db.install/attribute ?e]]
           db))


;; find jsut the data functions 
(sort (d/q '[:find [?ident ...]
             :where 
             [?e :db/ident ?ident]
             [_ :db.install/function ?e]]
           db))


;; documentation of a schema element
(-> db (d/entity :db.unique/identity) :db/doc)


;; complete details of a schema element
(-> db (d/entity :uuid) d/touch)

;; find all attributes w/ AVET index
(sort (d/q '[:find [?ident ...]
             :where
             [?e :db/ident ?ident]
             [?e :db/index true]
             [_ :db.install/attribute ?e]]
           db))

;; find attributes in the user namespace 
(sort (d/q '[:find [?ident ...]
             :where
             [?e :db/ident ?ident]
             [_ :db.install/attribute ?e]
             [(namespace ?ident) ?ns]
             [(= ?ns "match")]]
           db))

;; find all reference attributes
(sort (d/q '[:find [?ident ...]
             :where
             [?e :db/ident ?ident]
             [_ :db.install/attribute ?e]
             [?e :db/valueType :db.type/ref]]
           db))

;; cardinality-many
(sort (d/q '[:find [?ident ...]
             :where
             [?e :db/ident ?ident]
             [_ :db.install/attribute ?e]
             [?e :db/cardinality :db.cardinality/many]]
           db))


)