#!/usr/bin/groovy

def Map call(url, timestamp) {
  java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
  def trimedUrl = url.reverse().drop(4).reverse()
  def splitUrl = trimedUrl.drop(8).split("/")
  return [timestamp:sdf.format(timestamp),
          url:trimedUrl,
          owner:splitUrl[1],
          project:splitUrl[2]]
}
