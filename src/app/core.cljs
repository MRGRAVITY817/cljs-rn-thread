(ns app.core
  (:require [react-native :as rn]
            ["expo" :as expo]
            [uix.core :refer [$ defui] :as uix]
            [app.feed :refer [feed-screen]]
            [app.profile :refer [profile-screen]]))

(defui counter []
  (let [[count set-count!] (uix/use-state 0)]
    ($ rn/View {:style {:padding-top 44}}
       ($ rn/Text {:style {:font-size 18
                           :font-weight "500"
                           :text-align :center}}
          "You've counted to: " count)

       ($ rn/Pressable {:on-press #(set-count! inc)}
          ($ rn/Text {:style {:user-select "none"}}
             "Tap here to ++"))

       ($ rn/Pressable {:on-press #(set-count! dec)}
          ($ rn/Text {:style {:user-select "none"}}
             "Tap here to --")))))

(defui root []
  (let [[current-screen set-current-screen!] (uix/use-state {:screen :feed})

        navigate-to-profile (fn [username]
                              (set-current-screen! {:screen :profile
                                                    :username username}))

        navigate-back (fn []
                        (set-current-screen! {:screen :feed}))]

    (case (:screen current-screen)
      :feed ($ feed-screen {:on-profile-click navigate-to-profile})
      :profile ($ profile-screen {:username (:username current-screen)
                                  :on-back navigate-back}))))

(defn ^:export init []
  (expo/registerRootComponent root))
