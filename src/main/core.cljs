(ns main.core
  (:require [reagent.core :as reagent :refer [atom]]
            [secretary.core :as secretary :refer-macros [defroute]]
            [goog.events :as events]
            [goog.history.EventType :as EventType])
  (:import goog.History))

(def current-page (atom nil))

(defn handle-click []
  (js/alert "hello world")
  false)

(defn home-page []
  [:div "Home Page"])

(defn page-one []
  [:div "I am page one"])

(defn app-view []
  [:div [@current-page]])

(try
  (reagent/render-component [app-view] (.getElementById js/document "app"))
  (catch :default e e))

(defroute "/" []
  (.log js/console "home page")
  (reset! current-page home-page))

(defroute "/page-one" []
  (.log js/console "page-one")
  (reset! current-page page-one))

(secretary/set-config! :prefix "/")

; the server side doesn't have history, so we want to make sure current-page is populated
(reset! current-page home-page)

;(push-state! secretary/dispatch!
;  (fn [x] (when (secretary/locate-route x) x)))

;;; Quick and dirty history configuration.
(try
  (let [h (History.)]
    (goog.events/listen h EventType/NAVIGATE #(secretary/dispatch! (.-token %)))
    (doto h (.setEnabled true)))
  (catch :default e e))
