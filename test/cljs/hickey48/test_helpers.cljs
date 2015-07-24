(ns hickey48.test-helpers
  (:require [goog.dom :as dom]
            [reagent.core :as reagent]))

(defn found-in [re div]
  (reagent/flush)
  (let [res (.-innerHTML div)]
    (if (re-find re res)
      true
      (do (println "Not found: " res)
          false))))

(defn by-id
  "Return the element with the passed id."
  [id]
  (dom/getElement (name id)))
