#!/usr/bin/env python3
from tabulate import tabulate

# ------------------------------------------------------------------------------ 
# Search wikipedia synchronously
# ------------------------------------------------------------------------------ 
import requests
def search_wikipedia(searchterm, limit, namespace = 0):
    url = "https://en.wikipedia.org/w/api.php"
    params = {
        'action': 'opensearch',
        'search': searchterm,
        'limit': limit,
        'namespace': namespace,
        'format': 'json'
    }
    term, titles, descriptions, links = requests.get(url = url, params = params).json()
    return list(zip(titles, links))

# print(search_wikipedia('Bauer', 2))

# ------------------------------------------------------------------------------ 
# Search multiple wikipedia namespaces in parallel
# ------------------------------------------------------------------------------ 
import concurrent.futures as futures
def search_async(term, limit, namespaces = [0]):
    executor = futures.ThreadPoolExecutor(max_workers=len(namespaces))
    fs = [executor.submit(search_wikipedia, term, limit, ns) for ns in namespaces]
    done, not_done = futures.wait(fs)
    assert(len(not_done) == 0)
    results = []
    for f in done:
        results.extend(f.result())
    return results

# ------------------------------------------------------------------------------ 
# Print the result as a table
# ------------------------------------------------------------------------------ 
result = search_async('Bauer', 5, [0, 1, 2, 3])
print(tabulate(result, headers = ["Title", "Link"]))

