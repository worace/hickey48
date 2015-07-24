(ns ^:figwheel-always hickey48.core
    (:require [reagent.core :as reagent :refer [atom]]))

(enable-console-print!)

(defonce app-state (atom {:text "Hello world!"}))

(defn root []
  [:h1 (:text @app-state)])

(reagent/render-component [root]
                          (. js/document (getElementById "app")))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)

