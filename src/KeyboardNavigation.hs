module KeyboardNavigation (KeyPressed(..), handleKeyPress) where

import State
import Cursor
import Feed

data KeyPressed = KeyPressed Char


ifAllowed predicate operation x
  | predicate (operation x) = operation x
  | otherwise = x
cursorPositionsAreValid s = articleCursorPositionIsValid s && feedCursorPositionIsValid s

moveArticleCursorDown s = s { cursorArticles = Cursor ((position $ cursorArticles s) + 1) }
moveArticleCursorUp s = s { cursorArticles = Cursor ((position $ cursorArticles s) - 1) }

thereAreNoFeeds s = (length $ feeds s) == 0
unselectArticle s = s { cursorArticles = NothingSelected }
moveFeedCursorDown s  
  | thereAreNoFeeds s = s { cursorFeeds = NothingSelected }
  | noFeedSelected s = s { cursorFeeds = Cursor 0 }
  | otherwise =  s { cursorFeeds = Cursor ((position $ cursorFeeds s) + 1) } 
moveFeedCursorUp s  
  | thereAreNoFeeds s = s { cursorFeeds = NothingSelected }
  | noFeedSelected s =  s { cursorFeeds = Cursor ((length $ feeds s)-1) }
  | otherwise = s { cursorFeeds = Cursor ((position $ cursorFeeds s) - 1) }

handleKeyPress :: KeyPressed -> State -> State
handleKeyPress (KeyPressed 'j') s = ifAllowed cursorPositionsAreValid moveArticleCursorDown s
handleKeyPress (KeyPressed 'k') s = ifAllowed cursorPositionsAreValid moveArticleCursorUp s

handleKeyPress (KeyPressed 'f') s = 
  ifAllowed cursorPositionsAreValid (moveFeedCursorDown . unselectArticle) s
handleKeyPress (KeyPressed 'd') s = 
  ifAllowed cursorPositionsAreValid (moveFeedCursorUp . unselectArticle) s

handleKeyPress _ s = s

