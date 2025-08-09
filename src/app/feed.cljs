(ns app.feed
  (:require [app.data :refer [mock-posts]]
            [components.feed-header :refer [feed-header]]
            [components.feed-item :refer [feed-item]]
            [components.flat-list :refer [flat-list]]
            [react-native :as rn]
            [uix.core :refer [$ defui] :as uix]))

(defui feed [{:keys [on-profile-click new-post new-reply replying-to on-thread-click on-reply-click on-search-click on-hashtag-click]}]
  (let [[posts set-posts!] (uix/use-state mock-posts)

        ;; Effect to handle new posts
        _ (uix/use-effect
           (fn []
             (when new-post
               (set-posts! (fn [current-posts]
                             (let [next-id (if (empty? current-posts) 1 (+ (apply max (map :id current-posts)) 1))
                                   new-post-data {:id next-id
                                                  :user {:name "You"
                                                         :username "@you"
                                                         :avatar "https://via.placeholder.com/40"}
                                                  :content new-post
                                                  :timestamp "now"
                                                  :likes 0
                                                  :replies 0
                                                  :reposts 0
                                                  :liked? false
                                                  :reply-to nil
                                                  :thread-id next-id}]
                               (vec (cons new-post-data current-posts)))))))
           [new-post])

        ;; Effect to handle new replies
        _ (uix/use-effect
           (fn []
             (when (and new-reply replying-to)
               (set-posts! (fn [current-posts]
                             (let [next-id (if (empty? current-posts) 1 (+ (apply max (map :id current-posts)) 1))
                                   new-reply-data {:id next-id
                                                   :user {:name "You"
                                                          :username "@you"
                                                          :avatar "https://via.placeholder.com/40"}
                                                   :content new-reply
                                                   :timestamp "now"
                                                   :likes 0
                                                   :replies 0
                                                   :reposts 0
                                                   :liked? false
                                                   :reply-to (:id replying-to)
                                                   :thread-id (:thread-id replying-to)}
                                   updated-posts (vec (cons new-reply-data current-posts))
                                   ;; Update reply count on original post
                                   final-posts (mapv (fn [post]
                                                       (if (= (:id post) (:id replying-to))
                                                         (update post :replies inc)
                                                         post))
                                                     updated-posts)]
                               final-posts)))))
           [new-reply replying-to])

        handle-like (uix/use-callback
                     (fn [post-id]
                       (set-posts! (fn [current-posts]
                                     (mapv (fn [post]
                                             (if (= (:id post) post-id)
                                               (-> post
                                                   (update :liked? not)
                                                   (update :likes (if (:liked? post) dec inc)))
                                               post))
                                           current-posts))))
                     [])

        handle-reply (uix/use-callback
                      (fn [post-id]
                        (let [post (first (filter #(= (:id %) post-id) posts))]
                          (when (and post on-reply-click)
                            (on-reply-click post))))
                      [posts on-reply-click])

        handle-repost (uix/use-callback
                       (fn [post-id]
                         (js/console.log "Repost" post-id))
                       [])

        handle-hashtag-click (uix/use-callback
                              (fn [hashtag]
                                (when on-hashtag-click
                                  (on-hashtag-click hashtag)))
                              [on-hashtag-click])

        handle-mention-click (uix/use-callback
                              (fn [mention]
                                (js/console.log "Mention clicked:" mention))
                              [])]

;; Action handlers
    ($ rn/SafeAreaView {:style {:flex 1
                                :background-color "#ffffff"}}
       ($ feed-header {:on-search-click on-search-click})
       ($ flat-list {:data (filter #(nil? (:reply-to %)) posts) ; Only show main posts, not replies
                     :key-extractor (fn [item] (str (:id item)))
                     :render-item (fn [item]
                                    ($ feed-item {:post item
                                                  :on-like handle-like
                                                  :on-reply handle-reply
                                                  :on-repost handle-repost
                                                  :on-profile-click on-profile-click
                                                  :on-hashtag-click handle-hashtag-click
                                                  :on-mention-click handle-mention-click
                                                  :on-thread-click (fn []
                                                                     (when on-thread-click
                                                                       (on-thread-click (:thread-id item))))}))
                     :style {:flex 1}
                     :content-container-style {:padding-bottom 100}
                     :shows-horizontal-scroll-indicator false}))))

(defui compose-button [{:keys [on-press]}]
  ($ rn/View {:style {:position "absolute"
                      :bottom 20
                      :right 20}}
     ($ rn/TouchableOpacity {:style {:width 56
                                     :height 56
                                     :border-radius 28
                                     :background-color "#1d9bf0"
                                     :justify-content "center"
                                     :align-items "center"
                                     :shadow-color "#000"
                                     :shadow-offset {:width 0 :height 2}
                                     :shadow-opacity 0.25
                                     :shadow-radius 3.84
                                     :elevation 5}
                             :on-press on-press}
        ($ rn/Text {:style {:font-size 24
                            :color "white"}}
           "✏️"))))

(defui feed-screen [{:keys [on-profile-click on-compose-click on-thread-click on-reply-click on-search-click on-hashtag-click new-post new-reply replying-to]}]
  ($ rn/View {:style {:flex 1
                      :background-color "#ffffff"}}
     ($ feed {:on-profile-click on-profile-click
              :on-thread-click on-thread-click
              :on-reply-click on-reply-click
              :on-search-click on-search-click
              :on-hashtag-click on-hashtag-click
              :new-post new-post
              :new-reply new-reply
              :replying-to replying-to})
     ($ compose-button {:on-press on-compose-click})))
