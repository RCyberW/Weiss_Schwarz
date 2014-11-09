#!/usr/bin/python
import urllib2
import re
import sys
import os
import xml.dom

# boosterTypeArr = ['B', 'C', 'E', 'F']
boosterTypeArr = ['B']

def idFormating(boosterType, boosterRelease, cardNumber):
    newCardID = '{booster_id}-{card_id}-00';
    
    releaseNumber = '{0:02d}'
    boosterID = '{booster_type}{release_number}'
    cardID = '{0:03d}'
    
    releaseNumber = releaseNumber.format(boosterRelease)
    boosterID = boosterID.format(booster_type=boosterType, release_number=releaseNumber)
    cardID = cardID.format(cardNumber)
    
    newCardID = newCardID.format(booster_id=boosterID, card_id=cardID);
    return newCardID;

def connectToURL():
    # Constructing page URL for the HTML page info
    for boosterType in boosterTypeArr:
        for releaseNumber in range(1, 2):
            for nextIDSearch in range(1, 101):
                cardID = idFormating(boosterType, releaseNumber, nextIDSearch)
                cardURL = 'https://www.zxtcg.com/card/card_detail.php?n=' + cardID
                print cardURL
                request = urllib2.Request(cardURL)
                request.add_header('UserAgent', 'ZXTCG Scraper')
                response = urllib2.urlopen(cardURL)
                 
                parseToFile(cardID, response)
                extractImage(cardID)

def extractImage(cardID):
    try:
        imageURL = 'https://www.zxtcg.com/common/card_img/m/' + cardID + '_m.png'
        savedfile = cardID + '.png'
#         urllib.urlretrieve(imageURL, savedfile)
        print 'SUCCESS: ' + cardID
    except TypeError:
        print 'FAILED:  ' + cardID
        pass

def parseToFile(cardID, response):
    # Create directory    
    outputPathDir = 'ParsedFileOutput'
    outputCompleteDir = os.path.join(os.getcwd(), outputPathDir)
    
    print outputCompleteDir

    if not os.path.exists(outputCompleteDir):
        os.makedirs(outputPathDir)
    completeFileName = os.path.join(outputCompleteDir, cardID + '.html')
    
    f = open(completeFileName, 'w')
    writeMode = False
    
    # Parse through response to trim HTML data
    for line in response.read().split('\n'):
        if '<div id="lightBox">' in line:
            writeMode = True
            
        if writeMode:
            matchImgType = re.search('\w*_img', line) 
            if matchImgType is not None:
                imageType = matchImgType.group(0)
                line.replace(imageType, '')
                
                matchImg = re.search('(\w*-)*\w*.png', line)
                if matchImg is not None:
                    imageName = matchImg.group(0)
                    line.replace('.png', '')
            f.write(line + '\n')
            
        if '<!--/lightBox--></div>' in line:
            writeMode = False
            
    f.close()

def main():
    connectToURL()
    
main()

