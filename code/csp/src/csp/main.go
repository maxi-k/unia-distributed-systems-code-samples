package main

import (
	"fmt"
)

func main() {
	result, err := wikipedia("Larry Page", 5, 0)
	if err != nil {
		fmt.Errorf("error while fetching from wikipedia: %v", err)
	}
	fmt.Printf("Got Wikipedia Result:, %v", result)
}