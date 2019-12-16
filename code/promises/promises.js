const request = require('request-promise')

const wikipedia = (term, limit = 5) => {
    const namespace = 0
    const params = {
        url: "https://en.wikipedia.org/w/api.php",
        qs: {
            'action': 'opensearch',
            'search': term,
            'limit': limit,
            'namespace': namespace,
            'format': 'json'
        },
        json: true
    }
    return request(params)
}

const result = wikipedia("Larry Page")
    .then(res => res[2])
    .then(console.log)