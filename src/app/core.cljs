(ns app.core
  (:require [react-native :as rn]
            ["expo" :as expo]
            [uix.core :refer [$ defui] :as uix]
            [app.feed :refer [feed-screen]]
            [app.profile :refer [profile-screen]]
            [app.compose :refer [compose-screen]]))

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
        [posts set-posts!] (uix/use-state nil) ; For managing posts across screens

        navigate-to-profile (fn [username]
                              (set-current-screen! {:screen :profile
                                                    :username username}))

        navigate-to-compose (fn []
                              (set-current-screen! {:screen :compose}))

        navigate-back (fn []
                        (set-current-screen! {:screen :feed}))

        handle-post-submit (fn [post-content]
                             ;; This function will be passed down to update the feed
                             ;; For now, just navigate back - the actual post creation
                             ;; will be handled in the feed component
                             (set-current-screen! {:screen :feed :new-post post-content}))]

    (case (:screen current-screen)
      :feed ($ feed-screen {:on-profile-click navigate-to-profile
                            :on-compose-click navigate-to-compose
                            :new-post (:new-post current-screen)})
      :profile ($ profile-screen {:username (:username current-screen)
                                  :on-back navigate-back})
      :compose ($ compose-screen {:on-cancel navigate-back
                                  :on-submit handle-post-submit}))))

(defn ^:export init []
  (expo/registerRootComponent root))
