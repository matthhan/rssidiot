{-# LANGUAGE OverloadedStrings #-}
module Parsing (getArticles) where

import Text.HTML.TagSoup
import Text.StringLike
import Article

getArticles :: String -> [Article]
getArticles bareHtml = getResult  $ (getContainedTagsInEach "item") $  (parseTags bareHtml)


getContainedTagsInEach :: String -> [Tag String] -> [[Tag String]]
getContainedTagsInEach x [] =  []
getContainedTagsInEach requiredTagName soup = if firstContent /= [] then firstContent:(getContainedTagsInEach requiredTagName rest) else getContainedTagsInEach requiredTagName rest
  where 
    isStartTagOf (TagOpen thisTagName  _) 
      | thisTagName == requiredTagName = True 
      | otherwise = False
    isStartTagOf _ = False
    isEndtagOf (TagClose thisTagName) 
      | thisTagName == requiredTagName = True 
      | otherwise = False
    isEndtagOf _ = False
    droppingBreak x y =  (fst (break x y), (snd (break x y)))
    firstContent = fst (droppingBreak isEndtagOf (snd (droppingBreak isStartTagOf soup)))
    rest         = snd (droppingBreak isEndtagOf (snd (droppingBreak isStartTagOf soup)))


getResult :: [[Tag String]] -> [Article]
getResult x = map tagsToArticle x

tagsToArticle :: [Tag String] -> Article
tagsToArticle [] = Article [] [] False
tagsToArticle x =  Article (extractArticleName x) (extractArticleUrl x) False

extractArticleName :: [Tag String] -> String
extractArticleName = extractTextInFirstOccurenceOf "title"

extractArticleUrl :: [Tag String] -> String
extractArticleUrl = extractTextInFirstOccurenceOf "link"

extractTextInFirstOccurenceOf ::String -> [Tag String] -> String
extractTextInFirstOccurenceOf tag soup = textFromTextTag (extractRelevantTag soup)
  where 
    textFromTextTag (TagText x) = x
    extractRelevantTag x =  head  ((filter isTextTag) (head  (getContainedTagsInEach tag x)))
    isTextTag (TagText _) = True
    isTextTag _ = False
