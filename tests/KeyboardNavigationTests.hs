module KeyboardNavigationTests (testKeyboardNavigation)  where
import Test.Tasty.HUnit
import Test.Tasty
import TestCommon
import KeyboardNavigation
import State
import FeedsFixtures
import Cursor
import Debug.Trace


exampleState = State{feeds = [hackerNewsFeed, redditFeed, fefeFeed], cursorFeeds = Cursor 1, cursorArticles = Cursor 2}

testKeyboardNavigation = testGroup "Keyboard Navigation" [ 
    fMovesFeedCursorDown,
    dMovesFeedCursorUp,
    feedCursorCannotMoveIntoInvalidPosition,
    jMovesArticleCursorDown,
    kMovesArticleCursorUp,
    articleCursorCannotMoveIntoInvalidPosition
  ]
    
articleCursorPosition = position . cursorArticles 

jMovesArticleCursorDown =
  testCase
  "Pressing j moves the article cursor down"
  (assertThat ((articleCursorPosition afterJPressed) == (articleCursorPosition exampleState) + 1))
  where
    afterJPressed = handleKeyPress (KeyPressed 'j') exampleState 
kMovesArticleCursorUp = 
  testCase
  "Pressing k moves the article CursorUp"
  (assertThat ((articleCursorPosition afterKPressed) == (articleCursorPosition exampleState) - 1))
  where
    afterKPressed = handleKeyPress (KeyPressed 'k') exampleState 
articleCursorCannotMoveIntoInvalidPosition = 
  testCase
  "Any combination of pressing j and k can never result in an Article cursor with an invalid position"
  (assertThat (articleCursorPositionIsValid afterManyKeypresses))
  where
    afterManyKeypresses = (applyMultipleTimes 40 pressJ) ((applyMultipleTimes 30 pressK) exampleState)
    pressK = handleKeyPress (KeyPressed 'k')
    pressJ = handleKeyPress (KeyPressed 'j')
    applyMultipleTimes n f x = iterate f x !! n
fMovesFeedCursorDown = 
  testCase 
  "Pressing f moves the cursor for the feeds downwards"
  (assertThat ((position $ cursorFeeds afterFPressed) == (position $ cursorFeeds $ exampleState) + 1))
  where 
    afterFPressed = handleKeyPress (KeyPressed 'f') exampleState

dMovesFeedCursorUp = 
  testCase 
  "Pressing d moves the cursor for the feeds upwards"
  (assertThat ((position $ cursorFeeds afterDPressed) == (position $ cursorFeeds $ exampleState) - 1))
  where 
    afterDPressed = handleKeyPress (KeyPressed 'd') exampleState
feedCursorCannotMoveIntoInvalidPosition =
  testCase 
  "No combination of key presses can move the Feed cursor into an invalid position"
  (assertThat (feedCursorPositionIsValid afterManyKeypresses))
  where
    afterManyKeypresses = (applyMultipleTimes 40 pressF) ((applyMultipleTimes 30 pressD) exampleState)
    pressD = handleKeyPress (KeyPressed 'd')
    pressF = handleKeyPress (KeyPressed 'f')
    applyMultipleTimes n f x = iterate f x !! n

