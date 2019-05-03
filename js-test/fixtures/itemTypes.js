export const itemTypes = [
  {
    id: '29e5f3b8-735e-4eb3-a97d-917abaf14624',
    name: 'Apples',
  },
  {
    id: '076467ea-033b-423e-9dca-0faff6cd4b76',
    name: 'Bananas',
  },
  {
    id: '9cc0191b-2b12-4861-b552-9c3092bbbd6b',
    name: 'Granny smith',
  },
  {
    id: '5cbcd7a6-089a-423d-8b90-9fa025be594a',
    name: 'Lettuce',
  },
  {
    id: '464d2424-8e84-496a-9b2e-48aed96d6557',
    name: 'Pasta',
  },
  {
    id: '6cf57a3c-ae5c-463d-87f6-8cc8bae19ad8',
    name: 'Onion',
  },
  {
    id: '590f120d-a099-476f-b63e-f0e49fdeafa4',
    name: 'Tomatoes',
  },
];

export function getItemTypeByName(name) {
  return itemTypes.find(itemType => itemType.name === name);
}
