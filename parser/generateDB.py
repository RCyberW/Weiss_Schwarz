#!/usr/bin/python
import os
import re
import sys
import sqlite3
import codecs

print 'keyid,name,name_e,cardid,type,level,color,cost,trigger,power,soul,trait1,trait1_e,trait2,trait2_e,rule,rule_e,flavor,flavor_e,imagefilename'

conn = sqlite3.connect('testdb.sqlite')
conn.text_factory = str
c = conn.cursor()

c.execute('''create table cardtable
        (key_id INTEGER PRIMARY KEY AUTOINCREMENT,
        name TEXT,
        name_e TEXT,
        setid TEXT,
        cardid TEXT,
        type TEXT,
        level SMALLINT,
        color TEXT,
        cost SMALLINT,
        trigger TEXT,
        power INTEGER,
        soul SMALLINT,
        trait1 TEXT,
        trait1_e TEXT,
        trait2 TEXT,
        trait2_e TEXT,
        rule TEXT,
        rule_e TEXT,
        flavor TEXT,
        flavor_e TEXT,
        imagefilename TEXT,
        la_id INTEGER
        )''')

files = [e for e in os.listdir('.') if os.path.splitext(e)[1] == '.html']
la_ids = [int(re.search('\d+',e).group(0)) for e in files]
la_ids.sort()

dcw23ids = ['004','005','008','010','020','022','027','031','037','042','048','049','069','077','082','091','099']
skwe03ids = ['03','04','07','09','13']
skwe05ids = ['01','03','05','07','10','11','16','18','22','25','26','27','28','29','30','31','32','33']
rg10ids = ['110','111','112','113','114','115']
rg13ids = ['107','108','112','114']
usedImageNames = []


for la_id in la_ids:
    name = ''
    name_e = ''
    cardid = ''
    setid = ''
    ctype = ''
    level = ''
    color = ''
    cost = ''
    trigger = ''
    power = ''
    soul = ''
    trait1 = ''
    trait1_e = ''
    trait2 = ''
    trait2_e = ''
    rule = ''
    rule_e = ''
    flavor = ''
    flavor_e = ''
    imagefilename = ''
    fl = open('card'+str(la_id)+'.html','r')
    #fl = codecs.open(f,'r','utf-8')

    readingRule_e = False
    readingRule = False
    readingFlavor_e = False
    readingFlavor = False

    beginCardRead = False
    idRead = False

    for line in fl:
        if not beginCardRead:
            if 'card_details' in line:
                beginCardRead = True
            if 'Show ' in line:
                setid = re.search('<h5>[^<]*',line).group(0).replace('<h5>Show ','').replace('-','')
                if setid == 'DC/W17': setid = 'DC3/W18'
            continue
        elif '<h4>' in line:
            name = re.search('<h4>[^<]*',line).group(0).replace('<h4>','')
            name_e = re.search('<br />[^<]*',line).group(0).replace('<br />','')
        elif '<small>' in line and not idRead:
            cardid = re.sub('</?small>','',line).strip()
            if '/' not in cardid:
                cardid = setid+'-'+cardid
            if 'DC/W17' in cardid:
                cardid = cardid.replace('DC/W17','DC3/W18')
            if setid == 'DC/W23':
                for dcid in dcw23ids:
                    if dcid in cardid:
                        cardid = cardid.replace('DC/W23','DC3/W23')
                        setid = 'DC3/W23'
            if setid == 'SK & MH/WE03':
                for skid in skwe03ids:
                    if skid in cardid:
                        cardid = cardid.replace('SK & MH','SK')
                    else:
                        cardid = cardid.replace('SK & MH','MH')
            if setid == 'SK & MH/WE05':
                for skid in skwe05ids:
                    if skid in cardid:
                        cardid = cardid.replace('SK & MH','SK')
                    else:
                        cardid = cardid.replace('SK & MH','MH')

            if setid == 'RG & ID/W10':
                for rgid in rg10ids:
                    if rgid in cardid:
                        cardid = cardid.replace('RG & ID','RG')
                    else:
                        cardid = cardid.replace('RG & ID','ID')

            if setid == 'RG & ID/W13':
                for rgid in rg13ids:
                    if rgid in cardid:
                        cardid = cardid.replace('RG & ID','RG')
                    else:
                        cardid = cardid.replace('RG & ID','ID')


            imagefilename = re.sub(' ','_',cardid)
            imagefilename = re.sub('/','-',imagefilename)
            if imagefilename in usedImageNames:
                counter = 1
                if '/SE' in setid or '/WE' in setid:
                    imagefiletemp = imagefilename + '_holo'
                    while imagefiletemp in usedImageNames:
                        imagefiletemp = imagefilename + '_holo' + str(counter)
                        counter += 1
                    imagefilename = imagefiletemp
                else:
                    imagefiletemp = imagefilename + '_alt'
                    while imagefiletemp in usedImageNames:
                        imagefiletemp = imagefilename + '_alt' + str(counter)
                        counter += 1
                    imagefilename = imagefiletemp
            usedImageNames.append(imagefilename)
            imagefilename += '.jpg'
            print la_id, cardid, imagefilename
            idRead = True
        elif '<label>Type' in line:
            ctype = line.split()[-1].replace('</li>','')
        elif '<label>Level' in line:
            level = line.split()[-1].replace('</li>','')
        elif '<label>Color' in line:
            color = line.split()[-1].replace('</li>','')
        elif '<label>Cost' in line:
            cost = line.split()[-1].replace('</li>','')
        elif '<label>Trigger' in line:
            trigger = line.split()[-1].replace('</li>','')
        elif '<label>Power' in line:
            power = line.split()[-1].replace('</li>','')
        elif '<label>Soul' in line:
            soul = line.split()[-1].replace('</li>','')
        elif 'Traits:' in line:
            t1matcher = re.search('Cards1">[^<]*',line)
            t2matcher = re.search('Cards2">[^<]*',line)
            if t1matcher is not None:
                trait1 = t1matcher.group(0).replace('Cards1">','')
                trait1_e = re.search('</span> [^<]*',line).group(0).replace('</span> ','').replace('&laquo;','').replace('&#12298;','').strip()
            if t2matcher is not None:
                trait2 = t2matcher.group(0).replace('Cards2">','')
                trait2_e = re.search('&nbsp;[^<]*',line).group(0).replace('&nbsp;','').replace('&laquo;','').replace('&#12298;','').strip()
        elif 'Card Text' in line:
            readingRule_e = True
        elif readingRule_e:
            rule_e += re.sub('<[^<]+?>', '', line.replace('<br />','\n')).lstrip()
            if '</p>' in line:
                readingRule_e = False
                readingRule = True
        elif readingRule:
            rule += re.sub('<[^<]+?>', '', line.replace('<br />','\n')).lstrip()
            if '</p>' in line:
                readingRule = False
        elif 'Flavor Text' in line:
            readingFlavor_e = True
        elif readingFlavor_e:
            flavor_e += re.sub('<[^<]+?>', '', line.replace('<br />','\n')).strip()
            if '</p>' in line:
                readingFlavor_e = False
                readingFlavor = True
        elif readingFlavor:
            flavor += re.sub('<[^<]+?>', '', line.replace('<br />','\n')).strip()
            if '</p>' in line:
                readingFlavor = False

    #print '{keyid},{name},{name_e},{cardid},{ctype},{level},{color},{cost},{trigger},{power},{soul},{trait1},{trait1_e},{trait2},{trait2_e},{rule},{rule_e},{flavor},{flavor_e}'.format(keyid=keyid,name=name,name_e=name_e,cardid=cardid,ctype=ctype,level=level,color=color,cost=cost,trigger=trigger,power=power,soul=soul,trait1=trait1,trait1_e=trait1_e,trait2=trait2,trait2_e=trait2_e,rule=rule,rule_e=rule_e,flavor=flavor,flavor_e=flavor_e)
    if setid != '':
        c.execute('''
        insert into cardtable
        (name,name_e,setid,cardid,type,level,color,cost,trigger,power,soul,trait1,trait1_e,trait2,trait2_e,rule,rule_e,flavor,flavor_e,imagefilename,la_id)
        values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)''',
        (name,name_e,setid,cardid,ctype,level,color,cost,trigger,power,soul,trait1,trait1_e,trait2,trait2_e,rule,rule_e,flavor,flavor_e,imagefilename,la_id))

conn.commit()
conn.close()
