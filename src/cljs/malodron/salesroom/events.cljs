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
  :salesroom/toggle-shelf
  [->local-storage]
  (fn [{db :db} [_ cords]]
    (let [shelves (:salesroom/shelves db)
          floor (:salesroom/floor db)
          dispatch-n (if (contains? floor cords)
                       [[:salesroom/find-racks]]
                       [[:salesroom/toggle-floor cords]
                        [:salesroom/find-racks]])]
      {:db         (if (contains? shelves cords)
                     (update db :salesroom/shelves disj cords)
                     (update db :salesroom/shelves conj cords))
       :dispatch-n dispatch-n})))


(rf/reg-event-db
  :salesroom/set-active-tool
  (fn [db [_ tool]]
    (assoc db :salesroom/active-tool tool)))


(rf/reg-event-fx
  :salesroom/use-tool
  (fn [{db :db} [_ tool cords]]
    (let [event (case tool
                  :floor [:salesroom/toggle-floor cords]
                  :shelf [:salesroom/toggle-shelf cords])]
      {:dispatch event})))


(rf/reg-event-db
  :salesroom/find-racks
  (fn [db _]
    (let [size (:salesroom/size db)
          shelves (:salesroom/shelves db)
          racks (utils/shelves->racks size shelves)]
      (assoc db :salesroom/racks racks))))


(rf/reg-event-db
  :salesroom/set-rack-hovered
  (fn [db [_ rack]]
    (assoc db :salesroom/rack-hovered rack)))


(rf/reg-event-db
  :salesroom/set-rack-hovered
  (fn [db _]
    (assoc db :salesroom/rack-hovered nil)))


(rf/reg-event-db
  :salesroom/set-segment-hovered
  (fn [db [_ cords]]
    (println :salesroom/set-segment-hovered cords)
    (assoc db :salesroom/segment-hovered cords)))


(rf/reg-event-db
  :salesroom/reset-segment-hovered
  (fn [db _]
    (assoc db :salesroom/segment-hovered nil)))


(let [racks @(rf/subscribe [:salesroom/racks])]
  (loop [[rack & racks] racks rack-idx 0]
    (if rack
      (let [segment-idx (.indexOf rack [2 3])]
        (if-not (neg? segment-idx)
          [rack-idx segment-idx]
          (recur racks (inc rack-idx))))
      nil)))

(.indexOf [[1 2] [3 4]] [3 4])