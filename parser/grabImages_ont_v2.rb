#!/usr/bin/ruby

require 'Nokogiri'
require 'open-uri'

if ARGV.length != 1
    p 'Need keyword for search'
    exit 1
end

setid_re = ARGV[0].sub('/','').upcase
setid_hp = ARGV[0].sub('/','-').upcase

pageNo = 1

while true do
    doc = Nokogiri::HTML(
        open('http://orenoturn.com/?mode=srh&cid=&keyword='+setid_re+'&page='+pageNo.to_s),
        nil,'UTF-8')

    ids = doc.xpath("//div[@class='product_item']").map{ 
        |link| link.at('a')['href'].to_s.sub('?pid=','') }
    if ids.length == 0
        break
    end

    ids.each do |id|
        cardURL = 'http://orenoturn.com/?pid='+id
        cardid = Nokogiri::HTML(open(cardURL),nil,'UTF-8').at(
            "//td[@class='cell_2']").at('div').content.to_s.sub(setid_re,setid_hp)
        begin
            File.open(cardid+'.jpg','wb') do |file|
                file.write(
                    open('http://img15.shop-pro.jp/PA01242/155/product/'+id+'.jpg',
                        'rb').read)
                p 'Saved to ' + cardid+'.jpg ...'
            end
        rescue => e
            p 'Image not found for: ' + cardid
            File.delete(cardid+'.jpg') 
        end
    end
    pageNo += 1
end
