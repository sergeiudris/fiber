(ns ui.routes
  (:require [clojure.repl]
            [bidi.bidi :as bidi]
            [pushy.core :as pushy]
            [clerk.core :as clerk]
            #_[goog.events ]
            [reagent.core :as r]
            [ui.events :as events]
            [re-frame.core :as rf])
  #_(:import goog.History
           goog.history.Html5History
           goog.history.Html5History.TokenTransformer
           goog.history.EventType
           goog.Uri))

(def routes ["/" {""      :count
                  "count" :count
                  }])

#_(def _ (events/listen history EventType.NAVIGATE
                      (fn [e]
                        (when-let [match (-> (.-token e) match-fn identity-fn)]
                          (dispatch-fn match)))))

#_(defn ^:export clerk-nav
  [url]
  (clerk/navigate-page! url)
  )

#_(defn ^:export clerk-render
  []
  (r/after-render clerk/after-render!))

(defn- parse-url [url]
  (merge
   {:url url}
   (bidi/match-route routes url)))

(defn- dispatch-route [matched-route]
  (let [panel-name (keyword (str (name (:handler matched-route)) "-panel"))
        url (:url matched-route)]
    (prn "matched-route: " matched-route)
    #_(r/after-render clerk/after-render!)
    (rf/dispatch [::events/set-active-panel panel-name])
    #_(clerk/navigate-page! url)))

(defn app-routes []
  #_(clerk/initialize!)
  (pushy/start! (pushy/pushy dispatch-route parse-url)))

(def path-for (partial bidi/path-for routes))

