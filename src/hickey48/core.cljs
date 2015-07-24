(ns ^:figwheel-always hickey48.core
    (:require [reagent.core :as reagent :refer [atom]]))

(enable-console-print!)

(defn gen-board []
  (into {} (for [i (range 0 16)] {i 0})))

(defonce app (atom {:text "Hello world!"
                          :board (gen-board)}))

(defn set-value! [idx val]
  (swap! app update-in [:board] assoc idx val))

(defn get-value [idx] (get-in @app [:board idx]))

(defn square [[id value]]
  [:div {:class "square" :id (str "square-" id)} [:p value]])

(defn root []
   [:div {:id "game"} (map square (@app :board))])

(reagent/render-component [root]
                          (. js/document (getElementById "app")))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)

