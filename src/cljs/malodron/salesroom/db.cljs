(ns malodron.salesroom.db)


(def salesroom-state
  {:salesroom/name            "brak"
   :salesroom/id              0
   :salesroom/size            [10 10]
   :salesroom/floor           #{}
   :salesroom/racks           #{}
   :salesroom/rack-hovered    nil
   :salesroom/segment-hovered nil
   :salesroom/shelves         #{}
   :salesroom/active-tool     :floor})
