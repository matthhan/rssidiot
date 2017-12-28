{-# LANGUAGE OverloadedStrings #-}
module Parsing (getArticles) where

import Text.HTML.TagSoup
import Text.StringLike
import Data.Maybe
import Article

getArticles :: String -> [Article]
getArticles bareHtml = catMaybes $ (map tagsToArticle) $ (getContentOf "item") $  (parseTags bareHtml)


getContentOf :: String -> [Tag String] -> [[Tag String]]
getContentOf x [] =  []
getContentOf tag soup = 
  if startTagPresent && endTagPresent && firstContent /= [] then 
    firstContent:(getContentOf tag rest) 
  else 
    getContentOf tag rest
  where 
    isStartTagOf (TagOpen thisTagName  _) 
      | thisTagName == tag = True 
      | otherwise = False
    isStartTagOf _ = False
    isEndtagOf (TagClose thisTagName) 
      | thisTagName == tag = True 
      | otherwise = False
    isEndtagOf _ = False
    startTagPresent = (length (filter isStartTagOf soup) > 0)
    endTagPresent = (length (filter isEndtagOf soup) > 0)
    droppingBreak x y =  (fst (break x y), (snd (break x y)))
    firstContent = fst (droppingBreak isEndtagOf (snd (droppingBreak isStartTagOf soup)))
    rest         = snd (droppingBreak isEndtagOf (snd (droppingBreak isStartTagOf soup)))

tagsToArticle :: [Tag String] -> Maybe Article
tagsToArticle x =  createPossibleArticle (extractArticleName x) (extractArticleUrl x) 

createPossibleArticle :: String -> String -> Maybe Article
createPossibleArticle "" _ = Nothing
createPossibleArticle _ "" = Nothing
createPossibleArticle a b = Just (Article a b False)

extractArticleName :: [Tag String] -> String
extractArticleName = extractTextInFirstOccurenceOf "title"

extractArticleUrl :: [Tag String] -> String
extractArticleUrl = extractTextInFirstOccurenceOf "link"

extractTextInFirstOccurenceOf ::String -> [Tag String] -> String
extractTextInFirstOccurenceOf tag soup = textFromTextTag (extractRelevantTag soup)
  where 
    textFromTextTag (TagText x) = x
    extractRelevantTag x =  head  ((filter isTextTag) (head  (getContentOf tag x)))
    isTextTag (TagText _) = True
    isTextTag _ = False
