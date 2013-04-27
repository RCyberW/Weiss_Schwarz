#!/usr/bin/python
import urllib
import urllib2
import re
import sys
from bs4 import BeautifulSoup

# provide the set to pull as an argument in the form {setabbreviation}/{side+number

def main(setid):
    setid_re = setid.replace('/','')
    setid_hp = setid.replace('/','-')
    nextPage = 0

    while nextPage >= 0:
        listURL = 'http://orenoturn.com/goods_search.cgi?Keyword={kw}&pn={pn}'.format(kw=setid_re,pn=nextPage)

        response = urllib2.urlopen(listURL)
        soup = BeautifulSoup(response.read())
        s2 = soup.find_all('tr')

        # cards range from index 2-..., after that, it's pagination info
        idx = 2
        while setid_re+'-' in str(s2[idx]):
            # extract image
            card = s2[idx].find_all('td')
            cardid = ''
            try:
                cardid = card[1].span.contents[0].replace(setid_re,setid_hp)
                imagefile = card[0].a.img['src'].replace('./goods_image/','').replace('T1','Z1')
                imageURL = 'http://orenoturn.com/goods_image/' + imagefile
                savedfile = cardid+'.jpg'
                urllib.urlretrieve(imageURL,savedfile)
                print 'SUCCESS: ' + cardid
            except TypeError:
                print 'FAILED:  ' + cardid
                pass
            idx += 1

        # for next page info, find if the next page exists
        if nextPage+1 in [int(s.replace('pn=','')) for s in re.findall('pn=\d+',str(s2[idx]))]:
            nextPage += 1
        else:
            nextPage = -1

main(sys.argv[1])

