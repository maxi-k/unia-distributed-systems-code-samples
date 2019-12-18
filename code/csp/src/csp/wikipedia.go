package main

import (
	"fmt"
	"strconv"
	"io/ioutil"
	"net/http"
	"net/url"
	"encoding/json"
)

type WikipediaResult struct {
	searchterm string
	titles []string
	descriptions []string
	links []string
}

func wikipedia(language string, searchterm string, limit int, namespace int) (*WikipediaResult, error) {
	baseUrl, err := url.Parse(fmt.Sprintf("https://%s.wikipedia.org/w/api.php", language))
	if err != nil {
		return nil, err 
	}
	params := url.Values{}
	params.Add("action", "opensearch")
	params.Add("search", searchterm)
	params.Add("limit", strconv.Itoa(limit))
	params.Add("namespace", strconv.Itoa(namespace))
	params.Add("format", "json")

	baseUrl.RawQuery = params.Encode()

	client := http.Client{}

	req, err := http.NewRequest(http.MethodGet, baseUrl.String(), nil)
	if err != nil {
		return nil, err
	}
	req.Header.Set("Content-Type", "application/json")

	response, err := client.Do(req)
	if err != nil {
		return nil, err
	}

	body, err := ioutil.ReadAll(response.Body)
	if err != nil {
		return nil, err
	}

	var untypedResult []interface{}
	jsonErr := json.Unmarshal(body, &untypedResult)
	if jsonErr != nil {
		return nil, jsonErr
	}

	result := WikipediaResult{
	    untypedResult[0].(string),
	    makeStrings(untypedResult[1].([]interface{})),
	    makeStrings(untypedResult[2].([]interface{})),
	    makeStrings(untypedResult[3].([]interface{})),
	}

	return &result, nil
}

func makeStrings(data []interface{}) []string {
	aString := make([]string, len(data))
	for i, v := range data {
    	aString[i] = v.(string)
	}
	return aString
}