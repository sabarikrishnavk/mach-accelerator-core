
http://localhost:4000/
{
"Authorization":"Bearer eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJHYWxheHkiLCJzdWIiOiJ0ZXN0dXNlciIsIm5hbWUiOiJ0ZXN0dXNlciIsInVzZXJJZCI6IjZmNmU2YTg3LTMwNTctNGEwMi05MmQ1LWY0MDZlOTZjZTM4MSIsInJvbGUiOiJSRUdJU1RFUkVEIiwiZW1haWwiOiJ0ZXN0QHRlc3QuY29tIiwiaWF0IjoxNjIwNDI3MTI1LCJleHAiOjE2MjEwMzE5MjV9.EyTPOHcGSpHU8jWUrrgI--0qSX4mk6O599zzlDzgZiXv2pWDplxsrNbsCiNXQ8Z-zqWmC0rBd2ULCtgempulqQ"
}

query{
    inventorySkuLocation(skuid:"SKU1", location:"WH1"){
        totalQty
        location
    }
    inventoryBySku(skuid:"SKU1"){
        totalQty
        location
        skuid
    }
    skus(skuid:"SKU1"){
        name
        inventory{
            totalQty
        } 
    } 
}