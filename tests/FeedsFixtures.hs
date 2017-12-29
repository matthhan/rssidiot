module FeedsFixtures (hackerNewsFeed, redditFeed, fefeFeed) where
import Feed
import ArticlesFixtures

hackerNewsFeed = Feed { displayName = "Hacker News", url = "orange.website.com/rss", articles = hackerNewsArticles }
redditFeed = Feed { displayName = "Reddit Frontpage", url = "reddet.com/.rss", articles = redditArticles }
fefeFeed = Feed { displayName = "Fefes Blog", url = "blog.fefe.de", articles = fefeArticles }
