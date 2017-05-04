(ns malodron.salesroom.db)


(def salesroom-state
  {:salesroom/name             "brak"
   :salesroom/id               0
   :salesroom/size             [10 10]
   :salesroom/floor            #{}
   :salesroom/shelvings        #{}
   :salesroom/shelving-hovered nil
   :salesroom/segment-hovered  nil
   :salesroom/segments         #{}
   :salesroom/shelves          {}
   :salesroom/active-tool      :floor
   :salesroom/selected-segment nil
   :salesroom/product-path     nil
   :product/selected           nil
   :product/selected-path      nil})
