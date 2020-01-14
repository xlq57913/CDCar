# -*- coding: utf-8 -*-
# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: message_state.proto

from google.protobuf import descriptor as _descriptor
from google.protobuf import message as _message
from google.protobuf import reflection as _reflection
from google.protobuf import symbol_database as _symbol_database
# @@protoc_insertion_point(imports)

_sym_db = _symbol_database.Default()




DESCRIPTOR = _descriptor.FileDescriptor(
  name='message_state.proto',
  package='',
  syntax='proto2',
  serialized_options=None,
  serialized_pb=b'\n\x13message_state.proto\"?\n\x0b\x63\x61meraState\x12\x18\n\x10horizontalDegree\x18\x01 \x02(\x02\x12\x16\n\x0everticalDegree\x18\x02 \x02(\x02\"\x1d\n\x08\x61rmState\x12\x11\n\tarmDegree\x18\x01 \x02(\x02\"E\n\rmessage_state\x12\x1c\n\x06\x63\x61mera\x18\x01 \x02(\x0b\x32\x0c.cameraState\x12\x16\n\x03\x61rm\x18\x02 \x02(\x0b\x32\t.armState'
)




_CAMERASTATE = _descriptor.Descriptor(
  name='cameraState',
  full_name='cameraState',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='horizontalDegree', full_name='cameraState.horizontalDegree', index=0,
      number=1, type=2, cpp_type=6, label=2,
      has_default_value=False, default_value=float(0),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR),
    _descriptor.FieldDescriptor(
      name='verticalDegree', full_name='cameraState.verticalDegree', index=1,
      number=2, type=2, cpp_type=6, label=2,
      has_default_value=False, default_value=float(0),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  serialized_options=None,
  is_extendable=False,
  syntax='proto2',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=23,
  serialized_end=86,
)


_ARMSTATE = _descriptor.Descriptor(
  name='armState',
  full_name='armState',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='armDegree', full_name='armState.armDegree', index=0,
      number=1, type=2, cpp_type=6, label=2,
      has_default_value=False, default_value=float(0),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  serialized_options=None,
  is_extendable=False,
  syntax='proto2',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=88,
  serialized_end=117,
)


_MESSAGE_STATE = _descriptor.Descriptor(
  name='message_state',
  full_name='message_state',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='camera', full_name='message_state.camera', index=0,
      number=1, type=11, cpp_type=10, label=2,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR),
    _descriptor.FieldDescriptor(
      name='arm', full_name='message_state.arm', index=1,
      number=2, type=11, cpp_type=10, label=2,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  serialized_options=None,
  is_extendable=False,
  syntax='proto2',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=119,
  serialized_end=188,
)

_MESSAGE_STATE.fields_by_name['camera'].message_type = _CAMERASTATE
_MESSAGE_STATE.fields_by_name['arm'].message_type = _ARMSTATE
DESCRIPTOR.message_types_by_name['cameraState'] = _CAMERASTATE
DESCRIPTOR.message_types_by_name['armState'] = _ARMSTATE
DESCRIPTOR.message_types_by_name['message_state'] = _MESSAGE_STATE
_sym_db.RegisterFileDescriptor(DESCRIPTOR)

cameraState = _reflection.GeneratedProtocolMessageType('cameraState', (_message.Message,), {
  'DESCRIPTOR' : _CAMERASTATE,
  '__module__' : 'message_state_pb2'
  # @@protoc_insertion_point(class_scope:cameraState)
  })
_sym_db.RegisterMessage(cameraState)

armState = _reflection.GeneratedProtocolMessageType('armState', (_message.Message,), {
  'DESCRIPTOR' : _ARMSTATE,
  '__module__' : 'message_state_pb2'
  # @@protoc_insertion_point(class_scope:armState)
  })
_sym_db.RegisterMessage(armState)

message_state = _reflection.GeneratedProtocolMessageType('message_state', (_message.Message,), {
  'DESCRIPTOR' : _MESSAGE_STATE,
  '__module__' : 'message_state_pb2'
  # @@protoc_insertion_point(class_scope:message_state)
  })
_sym_db.RegisterMessage(message_state)


# @@protoc_insertion_point(module_scope)
