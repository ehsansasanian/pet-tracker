#!/usr/bin/env bash

API="http://localhost:8080/api/pets"

declare -a PAYLOADS=(
  '{"petType":"CAT","trackerType":"CAT_SMALL","ownerId":101,"inZone":true ,"lostTracker":false}'
  '{"petType":"CAT","trackerType":"CAT_SMALL","ownerId":102,"inZone":false,"lostTracker":false}'
  '{"petType":"CAT","trackerType":"CAT_BIG"  ,"ownerId":103,"inZone":true ,"lostTracker":false}'
  '{"petType":"CAT","trackerType":"CAT_BIG"  ,"ownerId":104,"inZone":false,"lostTracker":false}'

  '{"petType":"DOG","trackerType":"DOG_SMALL"  ,"ownerId":201,"inZone":true }'
  '{"petType":"DOG","trackerType":"DOG_SMALL"  ,"ownerId":202,"inZone":false}'

  '{"petType":"DOG","trackerType":"DOG_MEDIUM" ,"ownerId":203,"inZone":true }'
  '{"petType":"DOG","trackerType":"DOG_MEDIUM" ,"ownerId":204,"inZone":false}'
  '{"petType":"DOG","trackerType":"DOG_MEDIUM" ,"ownerId":205,"inZone":false}'

  '{"petType":"DOG","trackerType":"DOG_BIG"    ,"ownerId":206,"inZone":true }'
  '{"petType":"DOG","trackerType":"DOG_BIG"    ,"ownerId":207,"inZone":false}'
)

echo "Posting ${#PAYLOADS[@]} pets to $API …"
for BODY in "${PAYLOADS[@]}"; do
  curl -s -o /dev/null -w "⇢ %>s %b bytes\n" \
       -X POST "$API" \
       -H "Content-Type: application/json" \
       -d "$BODY"
done
echo "Done."
