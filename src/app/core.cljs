(ns app.core
  (:require [react-native :as rn]
            ["expo" :as expo]
            [uix.core :refer [$ defui] :as uix]
            [app.feed :refer [feed-screen]]
            [app.profile :refer [profile-screen]]
            [app.compose :refer [compose-screen]]
            [app.thread :refer [thread-screen]]
            [app.search :refer [search-screen]]
            [components.reply-composer :refer [reply-composer]]))

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

        navigate-to-thread (fn [thread-id]
                             (set-current-screen! {:screen :thread
                                                   :thread-id thread-id}))

        navigate-to-reply (fn [replying-to]
                            (set-current-screen! {:screen :reply
                                                  :replying-to replying-to}))

        navigate-to-search (fn []
                             (set-current-screen! {:screen :search}))

        navigate-to-hashtag (fn [hashtag]
                              (set-current-screen! {:screen :search
                                                    :search-query hashtag}))

        navigate-back (fn []
                        (set-current-screen! {:screen :feed}))

        handle-post-submit (fn [post-content]
                             ;; This function will be passed down to update the feed
                             ;; For now, just navigate back - the actual post creation
                             ;; will be handled in the feed component
                             (set-current-screen! {:screen :feed :new-post post-content}))

        handle-reply-submit (fn [reply-content]
                              ;; Handle reply submission
                              (set-current-screen! {:screen :feed :new-reply reply-content
                                                    :replying-to (:replying-to current-screen)}))]

    (case (:screen current-screen)
      :feed ($ feed-screen {:on-profile-click navigate-to-profile
                            :on-compose-click navigate-to-compose
                            :on-thread-click navigate-to-thread
                            :on-reply-click navigate-to-reply
                            :on-search-click navigate-to-search
                            :on-hashtag-click navigate-to-hashtag
                            :new-post (:new-post current-screen)
                            :new-reply (:new-reply current-screen)
                            :replying-to (:replying-to current-screen)})
      :profile ($ profile-screen {:username (:username current-screen)
                                  :on-back navigate-back})
      :compose ($ compose-screen {:on-cancel navigate-back
                                  :on-submit handle-post-submit})
      :thread ($ thread-screen {:thread-id (:thread-id current-screen)
                                :on-back navigate-back
                                :on-profile-click navigate-to-profile
                                :on-reply navigate-to-reply
                                :on-hashtag-click navigate-to-hashtag})
      :reply ($ reply-composer {:replying-to (:replying-to current-screen)
                                :on-cancel navigate-back
                                :on-submit handle-reply-submit})
      :search ($ search-screen {:on-back navigate-back
                                :on-profile-click navigate-to-profile
                                :on-thread-click navigate-to-thread
                                :initial-query (:search-query current-screen)}))))

(defn ^:export init []
  (expo/registerRootComponent root))
