package main

import (
	"fmt"
)

func main() {
	languages := []string{"de", "en", "es"}
	result, err := searchInLanguages("Bauer", languages)
	if err != nil {
		fmt.Printf("error while fetching from wikipedia: %v\n", err)
	}
	fmt.Println("\n--------------------------------------------------")
	fmt.Printf("Got Wikipedia Result from language %s.\n", result.language)
	fmt.Println("\n--------------------------------------------------")
	fmt.Println("Search links:")
	for _, link := range result.links {
		fmt.Printf("%s\n", link)
	}
	fmt.Println("\n--------------------------------------------------")
}

type LanguageResult struct {
	language string
	links []string
}

func searchInLanguages(searchterm string, languages []string) (*LanguageResult, error) {
	errorChan := make(chan error)
	resultChan := make(chan LanguageResult)
	for	_, lang := range languages {
		go func(searchLanguage string, searchTerm string) {
		  result, err := wikipedia(searchLanguage, searchTerm, 5, 0)
		  if err != nil {
		  	errorChan <- err
		  }
		  resultChan <- LanguageResult{searchLanguage, result.links}
		}(lang, searchterm)
	}
	select {
	case res := <-resultChan:
		return &res, nil
	case err := <-errorChan:
		return nil, err
	}
}
