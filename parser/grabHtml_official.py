#!/usr/bin/python
import urllib2
import re
import sys

def main(firstCard):
    nextCard = firstCard

    while nextCard != '':
        cardURL = 'http://ws-tcg.com/jsp/cardlist?cardno='+nextCard
        print cardURL
        request = urllib2.Request(cardURL)
        request.add_header('UserAgent', 'WSTCG Scraper')
        response = urllib2.urlopen(cardURL)
        f = open(nextCard.replace('/','-')+'.html','w')
        writeMode = False
        for line in response.read().split('\n'):
            if '<div id="cardDetail">' in line:
                writeMode = True
            if writeMode:
                if '&raquo;' in line:
                    matchObj = re.search('cardlist\?cardno=[^"]*',line)
                    if matchObj is not None:
                        nextCard = matchObj.group(0).replace('cardlist?cardno=','')
                    else:
                        nextCard = ''
                f.write(line)
            if '</div>' in line:
                writeMode = False
        f.close()


main(sys.argv[1])

