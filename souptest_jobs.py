'''
Created on Sep 27, 2017
@author: TT

Web scraping a page loaded by JS via XHR

* actual URLs removed

Uses requests library:
http://docs.python-requests.org/en/master/

and Beautiful Soup:
https://www.crummy.com/software/BeautifulSoup/

'''

from requests import Session
from bs4 import BeautifulSoup
from bs4 import SoupStrainer
import csv

def parsePage(page):
    strainedSoup2 = BeautifulSoup(page, "lxml", parse_only=onlysearchResults)    
    for li in strainedSoup2:
                
        # set default values in case they're not present in the results
        closingDate = ""
        jobtitle = ""
        joblink = ""
        organisation = ""
        langReq = ""
        salary = ""
        
        jobtitle = li.a.string
        joblink = baseUrl + li.a['href']
        
        # tablecells = li.find_all("div", class_="tableCell")
        # findall() returns a list like this:
        # [<div class="tableCell"> stuff <br/> more stuff </div>, <div class="tableCell">stuff <br/> more stuff </div>]
        # whereas find() returns the first instance directly
        firstcell = li.find("div", class_="tableCell")
    
        firstcolstrings = firstcell.get_text("\n", strip=True)
        #     print(firstcolstrings)
        firstcollist = firstcolstrings.splitlines()
        
        try:
            closingDate = firstcollist[0]
            closingDate = firstcollist[0]
            organisation = firstcollist[1]
            location = firstcollist[2]
        except IndexError:
            pass
        
        # remove text from the date
        closingDate = closingDate.replace("Closing date: ", "")
        
        # since find was used, advance from first item by sibling
        # see note "You might think ..." here:
        # https://www.crummy.com/software/BeautifulSoup/bs4/doc/#next-sibling-and-previous-sibling
        # for explanation of next line    
        secondcolstrings = firstcell.next_sibling.next_sibling.get_text("\n", strip=True)
        #     print(secondcolstrings)
        secondcollist = secondcolstrings.splitlines()
        
        try:
            langReq = secondcollist[0]
            salary = secondcollist[1]
        except IndexError:
            pass
        
        # print(jobtitle)
        # print(joblink)
        # print(closingDate)
        # print(organisation)
        # print(location)
        # print(langReq)
        # print(salary)
        # print("")
        
        # write csv in regular way (equivalent to single writerow() call below)
        #     csv1.write("\"" + jobtitle + "\"" +", ")
        #     csv1.write(joblink +",")
        #     csv1.write(closingDate +",")
        #     csv1.write(organisation +",") 
        #     csv1.write(location +",")
        #     csv1.write(langReq +",")
        #     csv1.write("\"" + salary + "\"" +",")
        #     csv1.write("\n")
        
#         print(", ".join([jobtitle, joblink, closingDate, organisation, location, langReq, salary]) + "\n")
        spamwriter.writerow([jobtitle, joblink, closingDate, organisation, location, langReq, salary])
    return


baseUrl = "removed"
searchUrl = baseUrl + 'removed'
requestUrl = searchUrl + 'removed'

header_useragent = {'User-Agent': 'Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Mobile Safari/537.36'}
header_Referer = {'Referer': searchUrl}

colNames = ["Pos Title", "Link", "Closing Date", "Company", "Loc", "Lang", "Salary"]

# Prepare CSV file
# old way, first try:
# create csv output file in regular way
# csvFilename = "jobsOutput1.csv"
# csv1 = open(csvFilename, "w")  # create or open csv, "w" to write strings
# csv1.write(", ".join(colNames))

# better way:
# create csv output file using csv.writer()
# (this writes a row at a time, take an iterable of items)
csvfile = open('jobsOutput.csv', 'w', newline='') 
spamwriter = csv.writer(csvfile)
spamwriter.writerow(colNames)

# testing
# tempTestFilename = "test.txt"
# pageBeforeSoup = open(tempTestFilename, "wb")

# filter the initial results to only the part of interest
# the list items with class of searchResult
onlysearchResults = SoupStrainer("li", class_="searchResult")

# filter pagelinks to find total num pages
onlyPageLinks = SoupStrainer("span", class_="pagelinks")


# Start the session

with Session() as s:
    # pretend it's a chrome instance
    s.headers.update(header_useragent)
    
    # 1st request
    r1 = s.get(searchUrl)
    #     print(r1.status_code)
    #     print(r1.request.headers)
    #     print(r1.cookies)
    
    # add in a referrer url
    s.headers.update(header_Referer)
    
    # 2nd request to setinternalaccess (?)
    r2 = s.get(searchUrl, json={'ajaxSetInternalAccess':'1'})
    #     print(r2.status_code)
    #     print(r2.request)
    #     print(r2.request.headers)
        
    # 3rd request, this time to the url that returns the results
    r3 = s.get(requestUrl)
    print(r3.status_code)
    #     print(r3.request)
    #     print(r3.request.headers)
    page = r3.content
       
    strainedSoup1 = BeautifulSoup(page, "lxml", parse_only=onlyPageLinks)
    links = strainedSoup1('a')
    # numPages always appears 3rd last, before Next, Last links
    numPages = int(links[-3].string)
    # print(numPages)
    
    # parse the first page
    parsePage(page)
    # start at page two to get the rest    
    for n in range(2, numPages+1):
        resp = s.get(requestUrl + "&requestedPage=" + str(n))
        print(resp.status_code)
        page = resp.content
        
        # testing
        #     pageBeforeSoup.write(page)
        #     f = open("soup_after_straining.txt", 'r')
        #     f = open("test2.txt", 'r')
        #     page = f.read()
        parsePage(page)