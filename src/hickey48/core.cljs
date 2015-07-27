(ns ^:figwheel-always hickey48.core
    (:require [reagent.core :as reagent :refer [atom]]
              [goog.events :as events]))

(enable-console-print!)

(defn sqrt [n] (.sqrt js/Math n))
(defn starter-val [] (rand-nth [2 2 2 4]))
(defn starter-positions [board-size]
  "Return a sequence of random indices and starter values
   where indices range the size of the board and starter vals
   are either 2 or 4. e.g. (15 2 3 4) -> index 15 is 2, index 3 is 4"
  (interleave (take 2 (shuffle (range 0 board-size)))
              (take 2 (repeatedly starter-val))))

(defn blank-board [board-size] (vec (take board-size (repeat 0))))

(defn gen-board [board-size]
    (apply assoc (blank-board board-size) (starter-positions board-size)))

(defn initial-state [] {:text "Hello world!" :board (gen-board 16)})

(defonce app (atom (initial-state)))

(defn set-value! [idx val] (swap! app update-in [:board] assoc idx val))

(defn paired [coll]
  "2048 only allows combining evenly paired groups of same numbers. So trying to combine
   2 2 2 0, for example, would result in one 4 and one leftover 2: 4 2 0 0. This function
   walks through a sequence partitioning the numbers into groups of at most 2 of the same value."
  (loop [pairs [] candidate (first coll) coll (rest coll)]
    (if (empty? coll)
      (if candidate (conj pairs [candidate]) pairs)
      (if (= candidate (first coll))
        (recur (conj pairs [candidate (first coll)]) (second coll) (drop 2 coll))
        (recur (conj pairs [candidate]) (first coll) (rest coll))))))

(def non-zero? (partial filter (comp not zero?)))

(defn padded [n coll] (take n (concat coll (repeat 0))))
(defn shift-group [vals]
  (padded (count vals)
          (map (partial reduce +)
               (paired (non-zero? vals)))))

(defn transpose [matrix] (apply map vector matrix))

(def directional-transforms
  {:left [identity]
   :right [(partial map reverse)]
   :up [transpose]
   :down [(partial map reverse) transpose]})

(defn directional-shift [dir rows]
  "Each directional shift is defined as a series of transformations onto
   the 2-d matrix representing the Board. To shift a board in the given direction,
   we need to a) apply the required transformations; b) shift the groups c) apply
   the required transformations in reverse in order to get back to the original board layout"
  (let [transforms (directional-transforms dir)
        forward (apply comp transforms)
        backward (apply comp (reverse transforms))]
    (backward (map shift-group (forward rows)))))

(defn shift-board [dir board]
  (let [row-size (sqrt (count board))
        rows (partition row-size board)]
    (vec (mapcat identity (directional-shift dir rows)))))


(def key-map
  {37 :left 38 :up 39 :right 40 :down
   ;; w - 87 a - 65 s - 83 d - 68
   87 :up 65 :left 83 :down 68 :right})

(def update-board! (partial swap! app assoc :board))

(defn with-randoms [board]
  (let [empty-positions (filter (comp not nil?) (for [i (range (count board))] (if (zero? (board i)) i)) )]
    (assoc board (first (shuffle empty-positions)) (starter-val))))

(defn move [key-code]
  (let [dir (key-map key-code)]
    (if dir
      (update-board!
       (with-randoms (shift-board dir (@app :board)) )))))

(events/listen js/document "keydown"
               (fn [e] (move (.-keyCode e))))

(defn square [id value]
  [:div {:class (str "square square-val-" value) :id (str "square-" id)} [:p ""]])

(defn root []
  [:div {:id "game"} (map-indexed square (@app :board))])

(reagent/render-component [root]
                          (. js/document (getElementById "app")))
