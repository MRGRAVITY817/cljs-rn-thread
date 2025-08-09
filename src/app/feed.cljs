(ns app.feed
  (:require [app.data :refer [mock-posts]]
            [components.feed-header :refer [feed-header]]
            [components.feed-item :refer [feed-item]]
            [components.flat-list :refer [flat-list]]
            [react-native :as rn]
            [uix.core :refer [$ defui] :as uix]))

(defui feed [{:keys [on-profile-click]}]
  (let [[posts set-posts!] (uix/use-state mock-posts)
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
                        (js/console.log "Reply to post" post-id))
                      [])

        handle-repost (uix/use-callback
                       (fn [post-id]
                         (js/console.log "Repost" post-id))
                       [])]

;; Action handlers
    ($ rn/SafeAreaView {:style {:flex 1
                                :background-color "#ffffff"}}
       ($ feed-header)
       ($ flat-list {:data posts
                     :key-extractor (fn [item] (str (:id item)))
                     :render-item (fn [item]
                                    ($ feed-item {:post item
                                                  :on-like handle-like
                                                  :on-reply handle-reply
                                                  :on-repost handle-repost
                                                  :on-profile-click on-profile-click}))
                     :style {:flex 1}
                     :content-container-style {:padding-bottom 100}
                     :shows-horizontal-scroll-indicator false}))))

(defui compose-button []
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
                             :on-press #(js/console.log "Compose new post")}
        ($ rn/Text {:style {:font-size 24
                            :color "white"}}
           "✏️"))))

(defui feed-screen [{:keys [on-profile-click]}]
  ($ rn/View {:style {:flex 1
                      :background-color "#ffffff"}}
     ($ feed {:on-profile-click on-profile-click})
     ($ compose-button)))
