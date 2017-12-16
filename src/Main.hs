{-# LANGUAGE OverloadedStrings #-}

module Main (main) where
import Network.HTTP.Simple
import Control.Monad.IO.Class
import qualified Data.ByteString.Char8 as B8

main :: IO ()
main = do
  result <- getContentFromUrl "https://news.ycombinator.com/rss" 
  B8.putStrLn(result)

getContentFromUrl url = do
  res <- httpBS url
  let resBody = getResponseBody res
  return resBody
