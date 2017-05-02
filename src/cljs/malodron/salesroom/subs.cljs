(ns malodron.salesroom.subs
  (:require [re-frame.core :as rf]
            [reagent.ratom :refer-macros [reaction]]
            [cuerdas.core :as str]
            [malodron.salesroom.utils :as utils]))


(rf/reg-sub
  :salesroom/size
  (fn [db _]
    (:salesroom/size db)))


(rf/reg-sub
  :salesroom/height
  (fn [db _]
    (get-in db [:salesroom/size 0])))


(rf/reg-sub
  :salesroom/width
  (fn [db _]
    (get-in db [:salesroom/size 1])))


(rf/reg-sub
  :salesroom/floor
  (fn [db _]
    (:salesroom/floor db)))


(rf/reg-sub
  :salesroom/shelves
  (fn [db _]
    (:salesroom/shelves db)))


(rf/reg-sub
  :salesroom/racks
  (fn [db _]
    (:salesroom/racks db)))


(rf/reg-sub-raw
  :salesroom/is-floor?
  (fn [db [_ cords]]
    (let [floor (:salesroom/floor @db)]
      (reaction (contains? floor cords)))))


(rf/reg-sub-raw
  :salesroom/is-rack?
  (fn [db [_ cords]]
    (let [rack (:salesroom/rack @db)]
      (reaction (contains? rack cords)))))


(rf/reg-sub-raw
  :salesroom/is-shelf?
  (fn [db [_ cords]]
    (let [shelves (:salesroom/shelves @db)]
      (reaction (contains? shelves cords)))))


(rf/reg-sub
  :salesroom/active-tool
  (fn [db _]
    (:salesroom/active-tool db)))


(rf/reg-sub-raw
  :salesroom/rack-idx
  (fn [db [_ rack]]
    (let [racks (:salesroom/racks @db)]
      (reaction (.indexOf racks rack)))))


(defn segment-idx [cords db]
  (let [racks (reaction (:salesroom/racks @db))]
    (println @racks)
    (loop [[rack & racks] @racks rack-idx 0]
      (if rack
        (let [segment-idx (.indexOf (sort rack) cords)]
          (if-not (neg? segment-idx)
            [rack-idx segment-idx]
            (recur racks (inc rack-idx))))
        nil))))

(rf/reg-sub-raw
  :salesroom/segment-idx
  (fn [db [_ cords]]
    (reaction (segment-idx cords db))))


(rf/reg-sub-raw
  :salesroom/segment-place
  (fn [db [_ cords]]
    (println :salesroom/segment-place cords)
    (let [idx (reaction (segment-idx cords db))]
      (reaction (str (str/upper (utils/int->char (first @idx))) (str/pad (str (second @idx)) {:length 2 :padding "0"}))))))

(rf/clear-subscription-cache!)
@(rf/subscribe [:salesroom/racks])
@(rf/subscribe [:salesroom/segment-idx [6 9]])