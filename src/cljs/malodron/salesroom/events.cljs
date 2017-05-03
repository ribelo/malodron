(ns malodron.salesroom.events
  (:require [re-frame.core :as rf]
            [malodron.db.core :refer [->local-storage]]
            [malodron.salesroom.utils :as utils]))


(rf/reg-event-db
  :salesroom/set-height
  [->local-storage]
  (fn [db [_ height]]
    (assoc-in db [:salesroom/size 0] height)))


(rf/reg-event-db
  :salesroom/set-width
  [->local-storage]
  (fn [db [_ width]]
    (assoc-in db [:salesroom/size 1] width)))


(rf/reg-event-db
  :salesroom/toggle-floor
  [->local-storage]
  (fn [db [_ cords]]
    (println :salesroom/toggle-floor cords)
    (let [floor (:salesroom/floor db)]
      (if (contains? floor cords)
        (update db :salesroom/floor disj cords)
        (update db :salesroom/floor conj cords)))))


(rf/reg-event-fx
  :salesroom/toggle-segment
  [->local-storage]
  (fn [{db :db} [_ cords]]
    (let [segments (:salesroom/segments db)
          floor (:salesroom/floor db)
          dispatch-n (if (contains? floor cords)
                       [[:salesroom/find-racks]]
                       [[:salesroom/toggle-floor cords]
                        [:salesroom/find-racks]])]
      {:db         (if (contains? segments cords)
                     (update db :salesroom/segments disj cords)
                     (update db :salesroom/segments conj cords))
       :dispatch-n dispatch-n})))


(rf/reg-event-db
  :salesroom/find-racks
  (fn [db _]
    (let [size (:salesroom/size db)
          segments (:salesroom/segments db)
          racks (utils/segments->racks size segments)]
      (assoc db :salesroom/shelvings racks))))


(rf/reg-event-db
  :salesroom/set-rack-hovered
  (fn [db [_ rack]]
    (assoc db :salesroom/shelving-hovered rack)))


(rf/reg-event-db
  :salesroom/set-rack-hovered
  (fn [db _]
    (assoc db :salesroom/shelving-hovered nil)))


(rf/reg-event-db
  :salesroom/set-segment-hovered
  (fn [db [_ cords]]
    (println :salesroom/set-segment-hovered cords)
    (assoc db :salesroom/segment-hovered cords)))


(rf/reg-event-db
  :salesroom/reset-segment-hovered
  (fn [db _]
    (assoc db :salesroom/segment-hovered nil)))


(rf/reg-event-db
  :salesroom/select-segment
  (fn [db [_ cords]]
    (assoc db :salesroom/selected-segment cords)))



(rf/reg-event-db
  :salesroom/reset-shelves
  (fn [db _]
    (assoc db :salesroom/shelves {})))


(rf/reg-event-db
  :salesroom/add-shelf-to-segment
  (fn [db [_ idx]]
    (let [segment-idx (:salesroom/selected-segment db)
          segment (get (:salesroom/shelves db) segment-idx [])]
      (assoc-in db [:salesroom/shelves segment-idx]
                (utils/insert-at-idx (or idx (count segment))
                                     [(utils/product)] segment)))))


(rf/reg-event-db
  :salesroom/remove-shelf-from-segment
  (fn [db [_ idx]]
    (let [segment-idx (:salesroom/selected-segment db)
          segment (get (:salesroom/shelves db) segment-idx [])]
      (assoc-in db [:salesroom/shelves segment-idx]
                (utils/remove-at-idx (or idx (dec (count segment))) segment)))))


(rf/reg-event-db
  :salesroom/add-product-to-shelf
  (fn [db [_ shelf-idx product-idx]]
    (let [segment-idx (:salesroom/selected-segment db)
          shelf (get-in (:salesroom/shelves db) [segment-idx shelf-idx] [])]
      (assoc-in db [:salesroom/shelves segment-idx shelf-idx]
                (utils/insert-at-idx (or product-idx (count shelf))
                                     (utils/product) shelf)))))


(rf/reg-event-fx
  :salesroom/remove-product-from-shelf
  (fn [{db :db} [_ shelf-idx product-idx]]
    (let [segment-idx (:salesroom/selected-segment db)
          shelf (get-in (:salesroom/shelves db) [segment-idx shelf-idx] [])
          new-shelf (utils/remove-at-idx (or product-idx (dec (count shelf))) shelf)]

      {:db         (assoc-in db [:salesroom/shelves segment-idx shelf-idx] new-shelf)
       :dispatch-n (if-not (seq new-shelf)
                     [[:salesroom/remove-shelf-from-segment shelf-idx]] [])})))


(rf/reg-event-db
  :salesroom/update-product-in-shelf
  (fn [db [_ shelf-idx product-idx product]]
    (let [segment-idx (:salesroom/selected-segment db)]
      {:db (assoc-in db [:salesroom/shelves segment-idx shelf-idx product-idx]
                     product)})))


;(rf/dispatch [:salesroom/reset-shelves])