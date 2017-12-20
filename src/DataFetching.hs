module DataFetching (getContentFromUrl) where

import Network.HTTP.Simple
import Control.Monad.IO.Class
import Control.Monad
import qualified Data.ByteString.Char8 as B8

getContentFromUrl :: String -> IO String
getContentFromUrl url = do
  res <- (httpBS (parseRequest_ url))
  return  ((B8.unpack . getResponseBody) res)

